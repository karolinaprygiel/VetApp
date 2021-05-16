package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VetRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.VetService;
import uj.jwzp2021.gp.VetApp.service.VisitService;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Slf4j
@RequestMapping("api/vets")
@RestController
public class VetRestController {

  private final VetService vetService;
  private final VisitService visitService;

  @Autowired
  public VetRestController(VetService vetService, VisitService visitService) {
    this.vetService = vetService;
    this.visitService = visitService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVet(@PathVariable int id) {
    log.info("Received GET request for /vets/"+ id);
    var a = vetService.getVetById(id);
    var x = representFull(a);
    return ResponseEntity.ok(x);
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    log.info("Received GET request for /vets");
    return ResponseEntity.ok(
        vetService.getAll().stream().map(this::representBrief).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<?> createVet(@RequestBody VetRequestDto vetRequestDto) {
    log.info("Received POST request for /vets with: " + vetRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(representFull(vetService.createVet(vetRequestDto)));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteVet(@PathVariable int id) {
    log.info("Received DELETE request for /vets/" + id);
    return ResponseEntity.ok(representFull(vetService.deleteVet(id)));
  }

  private VetRepresentation representBrief(Vet v) {
    var representation = VetRepresentation.fromVet(v);
    representation.add(linkTo(methodOn(VetRestController.class).getVet(v.getId())).withSelfRel());
    return representation;
  }

  private VetRepresentation representFull(Vet v) {
    var representation = VetRepresentation.fromVet(v);
    representation.add(linkTo(methodOn(VetRestController.class).getVet(v.getId())).withSelfRel());
    representation.add(
        v.getVisits().stream()
            .map(Visit::getId)
            .map(
                (visit) ->
                    linkTo(methodOn(VisitsRestController.class).getVisit(visit)).withRel("visits"))
            .collect(Collectors.toList()));
    return representation;
  }
}
