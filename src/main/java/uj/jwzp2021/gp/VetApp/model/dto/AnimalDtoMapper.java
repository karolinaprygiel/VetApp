package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

public class AnimalDtoMapper {
  public static Animal toAnimal(AnimalRequestDto animalRequestDto, Client owner) {
    return new Animal(
            -1,
            animalRequestDto.getType(),
            animalRequestDto.getName(),
            animalRequestDto.getYearOfBirth(),
            owner);
  }
}
