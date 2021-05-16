package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.AnimalRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

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
    return ResponseEntity.ok(representFull(animal));
  }

  @GetMapping
  public ResponseEntity<?> getAllAnimals() {
    return ResponseEntity.ok(
        animalService.getAllAnimals().stream()
            .map(this::representBrief)
            .collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequestDto animalRequestDto) {
    var result = animalService.createAnimal(animalRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body((representFull(result)));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    var animal = animalService.deleteAnimal(id);
    return ResponseEntity.ok(representFull(animal));
  }

  private AnimalRepresentation representBrief(Animal a) {
    var representation = AnimalRepresentation.fromAnimal(a);
    representation.add(
        linkTo(methodOn(AnimalRestController.class).getAnimal(a.getId())).withSelfRel());
    return representation;
  }

  private AnimalRepresentation representFull(Animal a) {
    var representation = AnimalRepresentation.fromAnimal(a);
    representation.add(
        linkTo(methodOn(AnimalRestController.class).getAnimal(a.getId())).withSelfRel());
    representation.add(
        linkTo(methodOn(ClientRestController.class).getClient(a.getOwner().getId()))
            .withRel("owner"));
    representation.add(
        a.getVisits().stream()
            .map(Visit::getId)
            .map(
                (visit) ->
                    linkTo(methodOn(VisitsRestController.class).getVisit(visit)).withRel("visits"))
            .collect(Collectors.toList()));
    return representation;
  }
}
