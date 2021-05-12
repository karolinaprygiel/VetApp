package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.AnimalRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.AnimalResponseDto;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/animals")
public class AnimalRestController {

  private final AnimalService animalService;

  @Autowired
  public AnimalRestController(AnimalService animalService) {
    this.animalService = animalService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getAnimal(@PathVariable int id) {
    var animal = animalService.getAnimalById(id);
    return ResponseEntity.ok(animal);
  }

  @GetMapping
  public ResponseEntity<?> getAllAnimals() {
    return ResponseEntity.ok(animalService.getAllAnimals());
  }

  @PostMapping
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequestDto animalRequestDto) {
    var result = animalService.createAnimal(animalRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body((result));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    var animal = animalService.deleteAnimal(id);
    return ResponseEntity.accepted().body(animal);
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<AnimalRepresentation> getAllHateoas() {
    var animals = animalService.getAllAnimals();
    return animals.stream().map(this::represent).collect(Collectors.toList());
  }

  private AnimalRepresentation represent(AnimalResponseDto a) {
    var representation = AnimalRepresentation.fromAnimal(a);
    representation.add(
        linkTo(methodOn(AnimalRestController.class).getAnimal(a.getId())).withSelfRel());
    representation.add(
        linkTo(methodOn(ClientRestController.class).getClient(a.getOwnerId())).withRel("owner"));
    return representation;
  }
}
