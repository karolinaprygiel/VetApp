package uj.jwzp2021.gp.VetApp.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/animals")
public class AnimalRestController {

  private final AnimalService animalService;
  private final ModelMapper modelMapper;

  @Autowired
  public AnimalRestController(AnimalService animalService, ModelMapper modelMapper) {
    this.animalService = animalService;
    this.modelMapper = modelMapper;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getAnimal(@PathVariable int id) {
    var animal = animalService.getAnimalById(id);
    if (animal.isPresent()) {
      var animalDto = AnimalMapper.toAnimalResponseDto(animal.get());
      return ResponseEntity.ok(animalDto);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(RestUtil.response("No animal with such ID found."));
  }

  @GetMapping
  public ResponseEntity<?> getAllAnimals() {
    return ResponseEntity.ok(animalService.getAllAnimals().stream().map(AnimalMapper::toAnimalResponseDto).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequestDto animalRequestDto) {
    var result = animalService.createAnimal(animalRequestDto);
    if (result.isPresent()) {
      return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(RestUtil.response("Owner with such id was not found."));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    return animalService
        .deleteAnimal(id)
        .map(animal -> ResponseEntity.accepted().body(animal))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
