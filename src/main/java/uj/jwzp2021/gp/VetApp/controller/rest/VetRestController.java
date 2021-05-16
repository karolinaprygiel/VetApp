package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VetRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.service.VetService;
import uj.jwzp2021.gp.VetApp.service.VisitService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    return ResponseEntity.ok(vetService.getVetById(id));
  }

  @GetMapping(path = "{id}/visits")
  public ResponseEntity<?> getVetVisits(@PathVariable int id) {
    return ResponseEntity.ok(
        vetService.getVetById(id).getVisitIds().stream()
            .map(visitService::getVisitById)
            .collect(Collectors.toList()));
  }

  @GetMapping(path = "{id}/hateoas")
  public ResponseEntity<?> getVetHateoas(@PathVariable int id) {
    return ResponseEntity.ok(represent(vetService.getVetById(id)));
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    return ResponseEntity.ok(vetService.getAll());
  }

  @PostMapping
  public ResponseEntity<?> createVet(@RequestBody VetRequestDto vetRequestDto) {
    var result = vetService.createVet(vetRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteVet(@PathVariable int id) {
    var result = vetService.deleteVet(id);
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<VetRepresentation> getAllHateoas() {
    var vets = vetService.getAll();
    return vets.stream().map(this::represent).collect(Collectors.toList());
  }

  private VetRepresentation represent(VetResponseDto v) {
    var representation = VetRepresentation.fromVetResponseDto(v);
    representation.add(linkTo(methodOn(VetRestController.class).getVet(v.getId())).withSelfRel());
    representation.add(
        linkTo(methodOn(VetRestController.class).getVetVisits(v.getId())).withRel("visits"));
    return representation;
  }
}
