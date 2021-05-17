package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VisitRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.VisitService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/visits", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VisitsRestController {

  private final VisitService visitService;

  @Autowired
  public VisitsRestController(VisitService visitService) {
    this.visitService = visitService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVisit(@PathVariable int id) {
    log.info("Received GET request for api/visits/" + id);
    var visitRepresentation = representFull(visitService.getVisitById(id));
    log.info("Returning httpStatus=200. Visit with id=" + id + " was found.");
    return ResponseEntity.ok(visitRepresentation);
  }

  @GetMapping
  public ResponseEntity<?> getAllVisits() {
    log.info("Received GET request for api/visits");
    var visitRepresentations = visitService.getAll().stream()
        .map(this::representBrief)
        .collect(Collectors.toList());
    log.info("Returning httpStatus=200, returning all visits");
    return ResponseEntity.ok(visitRepresentations);
  }

  @PostMapping()
  public ResponseEntity<?> createVisit(@RequestBody VisitRequestDto visitReq) {
    log.info("Received POST request for api/visits with: " + visitReq);
    var visitRepresentation = representFull(visitService.createVisit(visitReq));
    log.info("Returning httpStatus=201. Visit for request" + visitReq + " created successfully");
    return ResponseEntity.ok(visitRepresentation);
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable int id) {
    log.info("Received DELETE request for api/visits/" + id);
    var visitRepresentation = representFull(visitService.delete(id));
    log.info("Returning httpStatus=200. Visit with id=" + id +" deleted successfully");
    return ResponseEntity.ok(visitRepresentation);
  }

  @PatchMapping(path = "/{id}")
  ResponseEntity<?> update(
      @PathVariable int id, @RequestBody VisitUpdateRequestDto visitUpdateRequestDto) {
    log.info("Received PATCH request for api/visits/" + id + " with: " + visitUpdateRequestDto);
    var visitRepresentation= representFull(visitService.updateVisit(id, visitUpdateRequestDto));
    log.info("Returning httpStatus=200. Visit with id=" + id +" updated successfully with: " + visitUpdateRequestDto);
    return ResponseEntity.ok(visitRepresentation);
  }

  private VisitRepresentation representBrief(Visit v) {
    log.debug("Creating Brief Visit representation");
    var representation = VisitRepresentation.fromVisit(v);
    representation.add(
        linkTo(methodOn(VisitsRestController.class).getVisit(v.getId())).withSelfRel());
    return representation;
  }

  private VisitRepresentation representFull(Visit v) {
    log.debug("Creating Full Visit representation");
    var representation = VisitRepresentation.fromVisit(v);
    representation.add(
        linkTo(methodOn(VisitsRestController.class).getVisit(v.getId())).withSelfRel());
    representation.add(
        linkTo(methodOn(AnimalRestController.class).getAnimal(v.getAnimal().getId()))
            .withRel("animal"));
    representation.add(
        linkTo(methodOn(ClientRestController.class).getClient(v.getClient().getId()))
            .withRel("client"));
    representation.add(
        linkTo(methodOn(VetRestController.class).getVet(v.getVet().getId())).withRel("vet"));
    return representation;
  }

  @GetMapping("/find")
  public ResponseEntity<?> find(
      @RequestParam(value = "dateFrom") String from,
      @RequestParam(value = "dateTo") String to,
      @RequestParam(value = "duration") String duration_,
      @RequestParam(value = "preferredVet", required = false, defaultValue = "-1") String vetId_) {
    log.info(
        "Received GET request for api/find with parameters: (dateFrom:"
            + from
            + ", dateTo:"
            + to
            + ", duration:"
            + duration_
            + ", preferredVet:"
            + vetId_);
    LocalDateTime dateFrom = LocalDateTime.parse(from);
    LocalDateTime dateTo = LocalDateTime.parse(to);
    Duration duration = Duration.parse(duration_);
    int vetId = Integer.parseInt(vetId_);

    var visits = visitService.findVisits(dateFrom, dateTo, duration, vetId);
    log.info("Returning httpStatus=200. Returning possible visit's dates");
    return ResponseEntity.ok(visits);
  }
}
