package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

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
}
