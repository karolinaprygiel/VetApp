package uj.jwzp2021.gp.VetApp.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.core.Visit;
import uj.jwzp2021.gp.VetApp.services.VisitCreationResult;
import uj.jwzp2021.gp.VetApp.services.VisitService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/visits")
public class VisitsRestController {

    private final VisitService visitsService;

    @Autowired
    public VisitsRestController(VisitService visitsService) {
        this.visitsService = visitsService;
    }

    @GetMapping(path="{id}")
    public ResponseEntity<?> getVisit(@PathVariable int id) {
        return visitsService.getVisitById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Visit> getAllVisits() {
        return visitsService.getAllVisits();
    }

    @PostMapping()
    public ResponseEntity<?> createVisit(@RequestBody VisitRequest visitReq) {
        var result = visitsService.createVisit(visitReq);
        return result.map(this::visitCreationResultToBadRequest, this::visitToResult);
    }

    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable int id) {
        visitsService.delete(id);
    }

    private ResponseEntity<?> visitToResult(Visit visit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(visit);
    }

    private ResponseEntity<?> visitCreationResultToBadRequest(VisitCreationResult result) {
        switch (result) {
            case TOO_SOON:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"reason\": \"Booking for less than an hour in the future is prohibited.\"}");
            case OVERLAP:
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"reason\": \"Overlapping with other visit.\"}");
            case REPOSITORY_PROBLEM:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"reason\": \"Problem with server, please try again later.\"}");
            default:
                return ResponseEntity.badRequest().body("{\"reason\": \"Unknown.\"}");
        }
    }
}
