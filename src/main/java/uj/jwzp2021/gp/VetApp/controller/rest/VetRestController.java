package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VetRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.service.VetService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    return ResponseEntity.ok(vetService.getVetById(id));
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
    Link selfLink = linkTo(methodOn(VetRestController.class).getVet(v.getId())).withSelfRel();
    var representation = VetRepresentation.fromVetResponseDto(v);
    representation.add(selfLink);
    return representation;
  }
}
