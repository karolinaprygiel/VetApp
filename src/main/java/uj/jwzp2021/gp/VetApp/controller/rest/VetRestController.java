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

  @Autowired
  public VetRestController(VetService vetService) {
    this.vetService = vetService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVet(@PathVariable int id) {
    log.info("Received GET request for api/vets/"+ id);
    var vetRepresentation = representFull(vetService.getVetById(id));
    log.info("Returning httpStatus=200. Vet with id=" + id + " was found.");
    return ResponseEntity.ok(vetRepresentation);
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    log.info("Received GET request for api/vets");
    var vetRepresentation = vetService.getAll().stream().map(this::representBrief).collect(Collectors.toList());
    log.info("Returning httpStatus=200, returning all vets.");
    return ResponseEntity.ok(vetRepresentation);
  }

  @PostMapping
  public ResponseEntity<?> createVet(@RequestBody VetRequestDto vetRequestDto) {
    log.info("Received POST request for api/vets with: " + vetRequestDto);
    var vetRepresentation = representFull(vetService.createVet(vetRequestDto));
    log.info("Returning httpStatus=201. Vet for request" + vetRequestDto + " created successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(vetRepresentation);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteVet(@PathVariable int id) {
    log.info("Received DELETE request for api/vets/" + id);
    var vetRepresentation =  representFull(vetService.deleteVet(id));
    log.info("Returning httpStatus=200. Vet with id=" + id +" deleted successfully");
    return ResponseEntity.ok(vetRepresentation);
  }

  private VetRepresentation representBrief(Vet v) {
    log.debug("Creating Brief Vet representation");
    var representation = VetRepresentation.fromVet(v);
    representation.add(linkTo(methodOn(VetRestController.class).getVet(v.getId())).withSelfRel());
    return representation;
  }

  private VetRepresentation representFull(Vet v) {
    log.debug("Creating Full Vet representation");
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
