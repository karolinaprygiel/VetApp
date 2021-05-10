package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VisitRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.VisitRequest;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.VisitService;
import uj.jwzp2021.gp.VetApp.util.VisitCreationError;
import uj.jwzp2021.gp.VetApp.util.VisitUpdateError;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/visits", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitsRestController {

  private final VisitService visitsService;

  @Autowired
  public VisitsRestController(VisitService visitsService) {
    this.visitsService = visitsService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVisitById(@PathVariable int id) {
    return visitsService
        .getVisitById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
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

  @PatchMapping(path = "/{id}")
  ResponseEntity<?> update(@PathVariable int id, @RequestBody VisitRequest visitReq) {
    var result = visitsService.updateVisit(id, visitReq);
    return result.map(this::visitUpdateResultToBadRequest, this::visitToResult);
  }

  private ResponseEntity<?> visitToResult(Visit visit) {
    return ResponseEntity.status(HttpStatus.CREATED).body(visit);
  }

  private ResponseEntity<?> visitCreationResultToBadRequest(VisitCreationError result) {
    switch (result) {
      case STARTS_IN_PAST:
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(RestUtil.response("Can not book a visit in the past."));
      case TOO_SOON:
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(RestUtil.response("Booking for less than an hour in the future is prohibited."));
      case OVERLAP:
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(RestUtil.response("Overlapping with other visit."));
      case REPOSITORY_PROBLEM:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestUtil.response("Problem with server, please try again later."));
      default:
        return ResponseEntity.badRequest().body(RestUtil.response("Unknown error."));
    }
  }

  private ResponseEntity<?> visitUpdateResultToBadRequest(VisitUpdateError result) {
    switch (result) {
      case VISIT_NOT_FOUND:
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(RestUtil.response("Visit with such id was not found."));
      case ILLEGAL_FIELD:
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(RestUtil.response("You can modify only status and description."));
      case ILLEGAL_VALUE:
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(
                RestUtil.response(
                    "You can set status only to FINISHED, CANCELLED and NOT_APPEARED values."));
      case REPOSITORY_PROBLEM:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestUtil.response("Problem with server, please try again later."));
      default:
        return ResponseEntity.badRequest().body(RestUtil.response("Unknown error."));
    }
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<VisitRepresentation> getAllHateoas() {
    var visits = visitsService.getAllVisits();
    return visits.stream().map(this::represent).collect(Collectors.toList());
  }

  private VisitRepresentation represent(Visit v) {
    Link selfLink = linkTo(methodOn(VisitsRestController.class).getVisitById(v.getId())).withSelfRel();
    var representation = VisitRepresentation.fromVisit(v);
    representation.add(selfLink);
    return representation;
  }
}
