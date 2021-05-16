package uj.jwzp2021.gp.VetApp.controller.rest;

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
public class VisitsRestController {

  private final VisitService visitService;

  @Autowired
  public VisitsRestController(VisitService visitService) {
    this.visitService = visitService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVisit(@PathVariable int id) {
    return ResponseEntity.ok(representFull(visitService.getVisitById(id)));
  }

  @GetMapping
  public ResponseEntity<?> getAllVisits() {
    return ResponseEntity.ok(
        visitService.getAllVisits().stream()
            .map(this::representBrief)
            .collect(Collectors.toList()));
  }

  @PostMapping()
  public ResponseEntity<?> createVisit(@RequestBody VisitRequestDto visitReq) {
    return ResponseEntity.ok(representFull(visitService.createVisit(visitReq)));
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable int id) {
    return ResponseEntity.ok(representFull(visitService.delete(id)));
  }

  @PatchMapping(path = "/{id}")
  ResponseEntity<?> update(
      @PathVariable int id, @RequestBody VisitUpdateRequestDto visitUpdateRequestDto) {
    return ResponseEntity.ok(representFull(visitService.updateVisit(id, visitUpdateRequestDto)));
  }

  private VisitRepresentation representBrief(Visit v) {
    var representation = VisitRepresentation.fromVisit(v);
    representation.add(
        linkTo(methodOn(VisitsRestController.class).getVisit(v.getId())).withSelfRel());
    return representation;
  }

  private VisitRepresentation representFull(Visit v) {
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

    LocalDateTime dateFrom = LocalDateTime.parse(from);
    LocalDateTime dateTo = LocalDateTime.parse(to);
    Duration duration = Duration.parse(duration_);
    int vetId = Integer.parseInt(vetId_);

    var visits = visitService.findVisits(dateFrom, dateTo, duration, vetId);
    return ResponseEntity.ok(visits);
  }
}
