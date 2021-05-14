package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotAvailableException;
import uj.jwzp2021.gp.VetApp.exception.visit.*;
import uj.jwzp2021.gp.VetApp.mapper.VisitMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitDatesResponseDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.repository.VisitRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitService {

  private final VisitRepository visitRepository;
  private final AnimalService animalService;
  private final ClientService clientService;
  private final VetService vetService;
  private final OfficeService officeService;
  private final Clock clock;

  @Autowired
  public VisitService(
      VisitRepository visitRepository,
      AnimalService animalService,
      ClientService clientService,
      VetService vetService,
      OfficeService officeService,
      Clock clock
  ) {
    this.visitRepository = visitRepository;
    this.animalService = animalService;
    this.clientService = clientService;
    this.vetService = vetService;
    this.officeService = officeService;
    this.clock = clock;
  }


  private boolean dateTooSoon(LocalDateTime startTime) {
    return !LocalDateTime.now(clock).plusHours(1).isBefore(startTime);
  }

  private boolean dateInPast(LocalDateTime startTime) {
    return !LocalDateTime.now(clock).isBefore(startTime);
  }

  public List<VisitResponseDto> getAllVisits() {
    var visits = getAllRawVisits();
    return visits.stream().map(VisitMapper::toVisitResponseDto).collect(Collectors.toList());
  }

  List<Visit> getAllRawVisits() {
    return visitRepository.findAll().stream().sorted(Comparator.comparing(Visit::getStartTime)).collect(Collectors.toList());
  }

  public VisitResponseDto createVisit(VisitRequestDto req) {

    var client = clientService.getRawClientById(req.getClientId());
    var animal = animalService.getRawAnimalById(req.getAnimalId());
    var vet = vetService.getRawVetById(req.getVetId());
    var office = officeService.getRawOfficeById(req.getOfficeId());

    if (dateInPast(req.getStartTime())) {
      throw new VisitStartsInPastException("Visit can not start in the past.");
    }
    if (dateTooSoon(req.getStartTime())) {
      throw new VisitTooSoonException("Visit cannot be scheduled for less an hour from now on.");
    }

    if(!vetAvailable(req.getStartTime(), req.getDuration(), req.getVetId())){
      throw new VetNotAvailableException("Visit not in the working hours of the chosen vet");
    }

    if (!dateAvailable(req.getStartTime(), req.getDuration(), req.getVetId(), req.getOfficeId())) {
      throw new VisitOverlapsException("Visit would overlap with another visit. Try to change, date, vet or office");
    }

    if (animal.getOwner().getId() != client.getId()) {
      throw new WrongOwnerException("This person does not own this animal.");
    }

    var visit = visitRepository.save(VisitMapper.toVisit(req, animal, client, vet, office));
    return VisitMapper.toVisitResponseDto(visit);
  }

  private boolean dateAvailable(LocalDateTime startTime, Duration duration, int vetId, int officeId) {
    List<Visit> overlaps =
        visitRepository.overlaps(startTime, startTime.plusMinutes(duration.toMinutes()), vetId, officeId);
    return overlaps.size() == 0;
  }

  public VisitResponseDto delete(int id) {
    var visit = getRawVisitById(id);
    visitRepository.delete(visit);
    return VisitMapper.toVisitResponseDto(visit);
    }

  private boolean vetAvailable(LocalDateTime startTime, Duration duration, int vetId) {
    return vetService.vetAvailable(startTime, duration, vetId);
  }


  Visit getRawVisitById(int id) {
    var visit = visitRepository.findById(id);
    if (visit.isPresent()) {
      return visit.get();
    } else throw new VisitNotFoundException("Visit with id:" + id + " not found.");
  }

  public VisitResponseDto getVisitById(int id) {
    return VisitMapper.toVisitResponseDto(getRawVisitById(id));
  }

  @Scheduled(fixedRate = 3600000)
  public void finishOutOfDateVisits() {
    LocalDateTime time = LocalDateTime.now(clock);
    visitRepository.finishOutOfDateVisits(time);
    System.out.println("finishOutOfDateVisits function run at " + time);
  }

  public VisitResponseDto updateVisit(int id, VisitUpdateRequestDto visitReq) {
    var visit = getRawVisitById(id);
    if (visitReq.getVisitStatus() != null) {
      visit.setVisitStatus(visitReq.getVisitStatus());
    }
    if (visitReq.getDescription() != null) {
      visit.setDescription(visitReq.getDescription());
    }
    visitRepository.save(visit);
    return VisitMapper.toVisitResponseDto(visit);
  }

  public List<VisitDatesResponseDto> findVisits(LocalDateTime dateFrom, LocalDateTime dateTo, Duration duration, int vetId) {
    List<VisitDatesResponseDto> possibleVisits = new ArrayList<>();
    List<Vet> vets = new ArrayList<>();
    if (vetId != -1){
      var vet = vetService.getRawVetById(vetId);
      vets.add(vet);
    }else{
      vets.addAll(vetService.getRawAll());
    }
    List<Office> offices = officeService.getRawAll();
    LocalDateTime startTime = getStartTime(dateFrom, dateTo);

    while(startTime.plusMinutes(duration.toMinutes()).isBefore(dateTo.plusSeconds(1))){
      boolean foundDate = false;
      for (var vet : vets){
        for (var office : offices){
          foundDate = dateAvailable(startTime, duration, vet.getId(), office.getId()) && vetAvailable(startTime,duration, vet.getId());
          if(foundDate){
            possibleVisits.add(new VisitDatesResponseDto(startTime, duration, vet.getId(), office.getId()));
            break;
          }
        }
        if (foundDate){ break; }
      }
      if (foundDate){
        startTime = startTime.plusMinutes(15);
      }else{
        startTime = startTime.plusMinutes(5);
      }
    }
      return possibleVisits;
  }

  private LocalDateTime getStartTime(LocalDateTime dateFrom, LocalDateTime dateTo) {
    if (dateInPast(dateTo)){
      throw new VisitStartsInPastException("Cannot book visits in past");
    }else if (dateInPast(dateFrom)){
      return LocalDateTime.now(clock).plusMinutes(60).truncatedTo(ChronoUnit.MINUTES);
    }else{
      return dateFrom;
    }
  }
}
