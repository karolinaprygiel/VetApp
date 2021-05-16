package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VisitRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.AnimalService;
import uj.jwzp2021.gp.VetApp.service.VisitService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/visits", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitsRestController {

  private final VisitService visitService;
  private final AnimalService animalService;

  @Autowired
  public VisitsRestController(VisitService visitService, AnimalService animalService) {
    this.visitService = visitService;
    this.animalService = animalService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVisit(@PathVariable int id) {
    var result = visitService.getVisitById(id);
    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<?> getAllVisits() {
    //    return visitService.getAllVisits();
    var visits = visitService.getAll();
    return ResponseEntity.ok(visits);
  }

  @PostMapping()
  public ResponseEntity<?> createVisit(@RequestBody VisitRequestDto visitReq) {
    return ResponseEntity.ok(visitService.createVisit(visitReq));
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable int id) {
    return ResponseEntity.ok(visitService.delete(id));
  }

  @PatchMapping(path = "/{id}")
  ResponseEntity<?> update(
      @PathVariable int id, @RequestBody VisitUpdateRequestDto visitUpdateRequestDto) {
    var result = visitService.updateVisit(id, visitUpdateRequestDto);
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<VisitRepresentation> getAllHateoas() {
    var visits = visitService.getAll();
    return visits.stream().map(this::represent).collect(Collectors.toList());
  }

  private VisitRepresentation represent(Visit v) {
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
