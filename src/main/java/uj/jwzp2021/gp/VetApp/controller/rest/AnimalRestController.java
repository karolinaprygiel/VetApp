package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AnimalRestController {

  private final AnimalService animalService;

  @Autowired
  public AnimalRestController(AnimalService animalService) {
    this.animalService = animalService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getAnimal(@PathVariable int id) {
    log.info("Received GET request for api/animals/" + id);
    var animalRepresentation = representFull(animalService.getAnimalById(id));
    log.info("Returning httpStatus=200. Animal with id=" + id + " was found.");
    return ResponseEntity.ok(animalRepresentation);
  }

  @GetMapping
  public ResponseEntity<?> getAllAnimals() {
    log.info("Received GET request for api/animals");
    var animalRepresentations = animalService.getAllAnimals().stream()
        .map(this::representBrief)
        .collect(Collectors.toList());
    log.info("Returning httpStatus=200, returning all animals.");
    return ResponseEntity.ok(animalRepresentations);

  }

  @PostMapping
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequestDto animalRequestDto) {
    log.info("Received POST request for api/animals with: " + animalRequestDto);
    var animalRepresentation = representFull(animalService.createAnimal(animalRequestDto));
    log.info("Returning httpStatus=201. Animal for request" + animalRequestDto + " created successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(animalRepresentation);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    log.info("Received DELETE request for api/animals/" + id);
    var animalRepresentation = representFull(animalService.deleteAnimal(id));
    log.info("Returning httpStatus=200. Animal with id=" + id +" deleted successfully");
    return ResponseEntity.ok(animalRepresentation);
  }

  private AnimalRepresentation representBrief(Animal a) {
    log.debug("Creating Brief Animal representation");
    var representation = AnimalRepresentation.fromAnimal(a);
    representation.add(
        linkTo(methodOn(AnimalRestController.class).getAnimal(a.getId())).withSelfRel());
    return representation;
  }

  private AnimalRepresentation representFull(Animal a) {
    log.debug("Creating Full Animal representation");
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
