package uj.jwzp2021.gp.VetApp.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

import java.util.List;
import java.util.stream.Collectors;

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
    return ResponseEntity.ok(AnimalMapper.toAnimalResponseDto(animal));
  }

  @GetMapping
  public ResponseEntity<?> getAllAnimals() {
    return ResponseEntity.ok(animalService.getAllAnimals().stream().map(AnimalMapper::toAnimalResponseDto).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequestDto animalRequestDto) {
    var result = animalService.createAnimal(animalRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(AnimalMapper.toAnimalResponseDto(result));

  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    var animal = animalService.deleteAnimal(id);
    return ResponseEntity.accepted().body(AnimalMapper.toAnimalResponseDto(animal));
  }
}
