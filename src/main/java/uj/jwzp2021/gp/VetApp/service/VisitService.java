package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotAvailableException;
import uj.jwzp2021.gp.VetApp.exception.visit.*;
import uj.jwzp2021.gp.VetApp.mapper.VisitMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitDatesResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.repository.VisitRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VisitService {

  private final VisitRepository visitRepository;
  private final AnimalService animalService;
  private final ClientService clientService;
  private final VetService vetService;
  private final OfficeService officeService;
  private final VisitMapper visitMapper;
  private final Clock clock;

  @Autowired
  public VisitService(
      VisitRepository visitRepository,
      AnimalService animalService,
      ClientService clientService,
      VetService vetService,
      OfficeService officeService,
      VisitMapper visitMapper,
      Clock clock) {
    this.visitRepository = visitRepository;
    this.animalService = animalService;
    this.clientService = clientService;
    this.vetService = vetService;
    this.officeService = officeService;
    this.visitMapper = visitMapper;
    this.clock = clock;
  }


  public Visit getVisitById(int id) {
    log.info("Looking up visit with id=" + id);
    var visit = visitRepository.findById(id);
    return visit.orElseThrow(
        () -> {
          throw new VisitNotFoundException("Visit with id=" + id + " not found");
        });
  }


  public List<Visit> getAll() {
    log.info("Looking up all visits");
    return visitRepository.findAll().stream()
        .sorted(Comparator.comparing(Visit::getStartTime))
        .collect(Collectors.toList());
  }

  public Visit delete(int id) {
    log.info("Deleting visit with id=" + id);
    var visit = getVisitById(id);
    try{
      visitRepository.delete(visit);
    }catch (DataAccessException ex){
      log.error("Repository error while deleting visit with id=" + id);
      throw ex;
    }
    log.info("Vet with id=" + id + " deleted successfully");
    return visit;
  }

  public Visit createVisit(VisitRequestDto req) {

    var client = clientService.getClientById(req.getClientId());
    var animal = animalService.getAnimalById(req.getAnimalId());
    var vet = vetService.getVetById(req.getVetId());
    var office = officeService.getOfficeById(req.getOfficeId());

    if (animal.getOwner().getId() != client.getId()) {
      throw new WrongOwnerException("This person does not own this animal.");
    }

    if (dateInPast(req.getStartTime())) {
      throw new VisitStartsInPastException("Visit can not start in the past.");
    }
    if (dateTooSoon(req.getStartTime())) {
      throw new VisitTooSoonException("Visit cannot be scheduled for less an hour from now on.");
    }

    if (!vetAvailable(req.getStartTime(), req.getDuration(), vet)) {
      throw new VetNotAvailableException("Visit not in the working hours of the chosen vet.");
    }

    if (!dateAvailable(req.getStartTime(), req.getDuration(), req.getVetId(), req.getOfficeId())) {
      throw new VisitOverlapsException(
          "Visit would overlap with another visit. Try to change, date, vet or office.");
    }

    Visit visit;
    try{
     visit = visitRepository.save(visitMapper.toVisit(req, animal, client, vet, office));
    } catch(DataAccessException ex){
      log.error("Repository problem while saving visit for request: " + req);
      throw ex;
    }
    log.info("Visit for request: " + req + " created successfully");
    return visit;
  }

  private boolean dateAvailable(
      LocalDateTime startTime, Duration duration, int vetId, int officeId) {
    List<Visit> overlaps =
        visitRepository.overlaps(
            startTime, startTime.plusMinutes(duration.toMinutes()), vetId, officeId);
    return overlaps.size() == 0;
  }


  private boolean vetAvailable(LocalDateTime startTime, Duration duration, Vet vet) {
    return vetService.isVetAtWork(startTime, duration, vet);
    // return true;
  }

    private boolean dateTooSoon(LocalDateTime startTime) {
    return !LocalDateTime.now(clock).plusHours(1).isBefore(startTime);
  }

  private boolean dateInPast(LocalDateTime startTime) {
    return !LocalDateTime.now(clock).isBefore(startTime);
  }


  @Scheduled(fixedRate = 3600000)
  public void finishOutOfDateVisits() {
    LocalDateTime time = LocalDateTime.now(clock);
    visitRepository.finishOutOfDateVisits(time);
    log.info("finishOutOfDateVisits function run at " + time);
  }

  public Visit updateVisit(int id, VisitUpdateRequestDto visitReq) {
    log.info("Updating visit with id=" + id +" for: " + visitReq);
    var visit = getVisitById(id);
    if (visitReq.getVisitStatus() != null) {
      visit.setVisitStatus(visitReq.getVisitStatus());
    }
    if (visitReq.getDescription() != null) {
      visit.setDescription(visitReq.getDescription());
    }
    try{
    visitRepository.save(visit);
    } catch (DataAccessException ex) {
      log.error("Repository problem while updating visit with id=" + id +"  for request: " + visitReq);
      throw ex;
    }
    return visit;
  }

  public List<VisitDatesResponseDto> findVisits(LocalDateTime dateFrom, LocalDateTime dateTo, Duration duration, int vetId) {
    log.info("Finding available visits between {} and {} of duration {} preferred vet id: {}", dateFrom, dateTo, duration, vetId != -1 ? vetId : "Not specified");
    LocalDateTime startTime = getStartTime(dateFrom, dateTo);
    List<VisitDatesResponseDto> availableVisitTerms = new ArrayList<>();
    List<Vet> vets = getPossibleVets(vetId);
    List<Office> offices = officeService.getAll();
    Collections.shuffle(vets);
    Collections.shuffle(offices);


    while (startTime.plusMinutes(duration.toMinutes()).isBefore(dateTo.plusSeconds(1))) {
      boolean isDateAvailable = false;
      outerloop:
      for (var vet : vets) {
        if (!vetAvailable(startTime, duration, vet)){
          continue;
        }
        for (var office : offices) {
          isDateAvailable = dateAvailable(startTime, duration, vet.getId(), office.getId());
          if (isDateAvailable) {
            availableVisitTerms.add(new VisitDatesResponseDto(startTime, duration, vet.getId(), office.getId()));
            break outerloop;
          }
        }
      }
      if (isDateAvailable) {
        startTime = startTime.plusMinutes(15);
      } else {
        startTime = startTime.plusMinutes(5);
      }
    }
    return availableVisitTerms;
  }


  private List<Vet> getPossibleVets(int vetId) {
    List<Vet> vets = new ArrayList<>();
    if (vetId != -1) {
      var vet = vetService.getVetById(vetId);
      vets.add(vet);
    } else {
      vets.addAll(vetService.getAll());
    }
    return vets;
  }

  private LocalDateTime getStartTime(LocalDateTime dateFrom, LocalDateTime dateTo) {
    if (dateInPast(dateTo)) {
      throw new VisitStartsInPastException("Cannot book visits in past");
    } else if (dateInPast(dateFrom)) {
      return LocalDateTime.now(clock).plusMinutes(60).truncatedTo(ChronoUnit.MINUTES);
    } else {
      return dateFrom;
    }
  }
}
