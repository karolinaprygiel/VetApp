package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VisitRepresentation;
import uj.jwzp2021.gp.VetApp.mapper.VisitMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.AnimalService;
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

  @GetMapping(path = "{id}/animal")
  public ResponseEntity<?> getVisitAnimal(@PathVariable int id) {
    var animal = animalService.getAnimalById(visitService.getVisitById(id).getAnimalId());
    return ResponseEntity.ok(animal);
  }

  @GetMapping
  public ResponseEntity<?> getAllVisits() {
    //    return visitService.getAllVisits();
    var visits = visitService.getAllVisits();
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
    var visits = visitService.getAllVisits();
    return visits.stream().map(this::represent).collect(Collectors.toList());
  }

  private VisitRepresentation represent(VisitResponseDto v) {
    var representation = VisitRepresentation.fromVisit(v);
    representation.add(
        linkTo(methodOn(VisitsRestController.class).getVisit(v.getId())).withSelfRel());
    representation.add(
        linkTo(methodOn(AnimalRestController.class).getAnimal(v.getAnimalId())).withRel("animalDetached"));
    representation.add(
        linkTo(methodOn(VisitsRestController.class).getVisitAnimal(v.getId())).withRel("animal"));
    representation.add(
        linkTo(methodOn(ClientRestController.class).getClient(v.getClientId())).withRel("client"));
    representation.add(
        linkTo(methodOn(VetRestController.class).getVet(v.getVetId())).withRel("vet"));
    return representation;
  }

}
