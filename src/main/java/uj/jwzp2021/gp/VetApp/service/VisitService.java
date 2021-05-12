package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.VisitNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.VisitOverlapsException;
import uj.jwzp2021.gp.VetApp.exception.VisitStartsInPastException;
import uj.jwzp2021.gp.VetApp.exception.VisitTooSoonException;
import uj.jwzp2021.gp.VetApp.model.dto.VisitMapper;
import uj.jwzp2021.gp.VetApp.model.dto.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.VisitResponseDto;
import uj.jwzp2021.gp.VetApp.model.dto.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.repository.VisitRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitService {

  private final VisitRepository visitRepository;
  private final AnimalService animalService;
  private final ClientService clientService;
  private final VetService vetService;

  @Autowired
  public VisitService(
      VisitRepository visitRepository,
      AnimalService animalService,
      ClientService clientService,
      VetService vetService) {
    this.visitRepository = visitRepository;
    this.animalService = animalService;
    this.clientService = clientService;
    this.vetService = vetService;
  }

  private static boolean dateTooSoon(LocalDateTime startTime) {
    return !LocalDateTime.now().plusHours(1).isBefore(startTime);
  }

  private static boolean dateInPast(LocalDateTime startTime) {
    return !LocalDateTime.now().isBefore(startTime);
  }

  public List<Visit> getAllVisits() {
    return visitRepository.findAll();
  }

  public VisitResponseDto createVisit(VisitRequestDto req) {
    if (dateInPast(req.getStartTime())) {
      throw new VisitStartsInPastException("Visit can not start in the past.");
    }
    if (dateTooSoon(req.getStartTime())) {
      throw new VisitTooSoonException("Visit cannot be scheduled for less an hour from now on.");
    }
    if (!dateAvailable(req.getStartTime(), req.getDuration())) {
      throw new VisitOverlapsException("Visit would overlap with another visit.");
    }
    var client = clientService.getRawClientById(req.getClientId());
    var animal = animalService.getRawAnimalById(req.getAnimalId());
    var vet = vetService.getRawVetById(req.getVetId());

    var visit = visitRepository.save(VisitMapper.toVisit(req, animal, client, vet));
    return VisitMapper.toVisitResponseDto(visit);
  }

  private boolean dateAvailable(LocalDateTime startTime, Duration duration) {
    List<Visit> overlaps =
        visitRepository.overlaps(startTime, startTime.plusMinutes(duration.toMinutes()));
    return overlaps.size() == 0;
  }

  public boolean delete(int id) {
    var visit = visitRepository.findById(id);
    if (visit.isPresent()) {
      visitRepository.deleteById(visit.get().getId());
      return true;
    } else {
      return false;
    }
  }

  public Visit getRawVisitById(int id) {
    var visit = visitRepository.findById(id);
    if (visit.isPresent()) {
      return visit.get();
    } else throw new VisitNotFoundException("Visit with id:" + id + " not found.");
  }

  public VisitResponseDto getVisitById(int id) {
    var visit = visitRepository.findById(id);
    if (visit.isPresent()) {
      return VisitMapper.toVisitResponseDto(visit.get());
    } else throw new VisitNotFoundException("Visit with id:" + id + " not found.");
  }

  @Scheduled(fixedRate = 3600000)
  public void finishOutOfDateVisits() {
    LocalDateTime time = LocalDateTime.now();
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
}
