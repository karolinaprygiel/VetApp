package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.model.dto.VisitRequest;
import uj.jwzp2021.gp.VetApp.model.entity.Status;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.repository.VisitRepository;
import uj.jwzp2021.gp.VetApp.util.OperationResult;
import uj.jwzp2021.gp.VetApp.util.VisitCreationError;
import uj.jwzp2021.gp.VetApp.util.VisitUpdateError;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

  private final VisitRepository visitRepository;

  @Autowired
  public VisitService(VisitRepository visitRepository) {
    this.visitRepository = visitRepository;
  }

  public List<Visit> getAllVisits() {
    return visitRepository.findAll();
  }

  public OperationResult<VisitCreationError, Visit> createVisit(VisitRequest req) {
    if (dateInPast(req.getStartTime())) {
      return OperationResult.fail(VisitCreationError.STARTS_IN_PAST);
    }
    if (dateTooSoon(req.getStartTime())) {
      return OperationResult.fail(VisitCreationError.TOO_SOON);
    }
    if (!dateAvailable(req.getStartTime(), req.getDuration())) {
      return OperationResult.fail(VisitCreationError.OVERLAP);
    }
    Visit v =
        visitRepository.save(
            Visit.newVisit(
                req.getStartTime(),
                req.getDuration(),
                req.getAnimal(),
                req.getPrice(),
                req.getClient(),
                req.getVet()));

    return OperationResult.success(v);
  }

  private static boolean dateTooSoon(LocalDateTime startTime) {
    return !LocalDateTime.now().plusHours(1).isBefore(startTime);
  }

  private static boolean dateInPast(LocalDateTime startTime) {
    return !LocalDateTime.now().isBefore(startTime);
  }

  private boolean dateAvailable(LocalDateTime startTime, Duration duration) {
    List<Visit> overlaps =
        visitRepository.overlaps(startTime, startTime.plusMinutes(duration.toMinutes()));
    overlaps.forEach(System.out::println);
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

  public Optional<Visit> getVisitById(int id) {
    return visitRepository.findById(id);
  }

  @Scheduled(fixedRate = 3600000)
  public void finishOutOfDateVisits() {
    LocalDateTime time = LocalDateTime.now();
    visitRepository.finishOutOfDateVisits(time);
    System.out.println("finishOutOfDateVisits function run at " + time);
  }

  public OperationResult<VisitUpdateError, Visit> updateVisit(int id, VisitRequest visitReq) {
    if (visitReq.getAnimal() != null
        || visitReq.getClient() != null
        || visitReq.getPrice() != null
        || visitReq.getDuration() != null
        || visitReq.getStartTime() != null
        || visitReq.getVet() != null) {

      return OperationResult.fail(VisitUpdateError.ILLEGAL_FIELD);
    }

    var visit = visitRepository.findById(id);
    if (visit.isPresent()) {
      if (visitReq.getDescription() != null) {
        visit.get().setDescription(visitReq.getDescription());
      }
      if (visitReq.getStatus() != null) {
        Status status = visitReq.getStatus();
        if (status != Status.CANCELLED
            && status != Status.FINISHED
            && status != Status.NOT_APPEARED) {
          return OperationResult.fail(VisitUpdateError.ILLEGAL_VALUE);
        }
        visit.get().setStatus(visitReq.getStatus());
      }
      Visit v = visitRepository.save(visit.get());

      return OperationResult.success(v);
    }
    return OperationResult.fail(VisitUpdateError.VISIT_NOT_FOUND);
  }
}
