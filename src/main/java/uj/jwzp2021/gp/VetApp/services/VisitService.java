package uj.jwzp2021.gp.VetApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.controllers.rest.VisitRequest;
import uj.jwzp2021.gp.VetApp.core.Visit;
import uj.jwzp2021.gp.VetApp.repos.VisitRepository;
import uj.jwzp2021.gp.VetApp.util.OpResult;

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

    public OpResult<VisitCreationResult, Visit> createVisit(VisitRequest req) {
        if (dateTooSoon(req.getStartTime())) {
            return OpResult.fail(VisitCreationResult.TOO_SOON);
        } else if (!dateAvailable(req.getStartTime(), req.getDuration())) {
            return OpResult.fail(VisitCreationResult.OVERLAP);
        } else {
            Visit v = visitRepository.save(Visit.newVisit(req.getStartTime(), req.getDuration(), req.getAnimal(), req.getPrice(), req.getClient(), req.getVet()));
            return OpResult.success(v);
        }
    }

    private boolean dateTooSoon(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        return !LocalDateTime.now().plusHours(1).isBefore(startTime);
    }

    private boolean dateAvailable(LocalDateTime startTime, Duration duration) {
        List<Visit> overlaps = visitRepository.overlaps(startTime, startTime.plusMinutes(duration.toMinutes()));
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
    public void finishOutOfDateVisits(){
        LocalDateTime time = LocalDateTime.now();
        visitRepository.finishOutOfDateVisits(time);
        System.out.println("finishOutOfDateVisits function run at " + time);

    }
}
