package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

import static org.springframework.util.StringUtils.capitalize;

public class AnimalMapper {
  public static AnimalResponseDto toAnimalResponseDto(Animal animal) {
    return new AnimalResponseDto(
        animal.getId(),
        capitalize(animal.getType().toString()),
        animal.getName(),
        animal.getYearOfBirth(),
        animal.getOwner().getId(),
        animal.getOwner().getName(),
        animal.getOwner().getSurname());
  }

  public static Animal toAnimal(AnimalRequestDto animalRequestDto, Client owner) {
    return new Animal(
            -1,
            animalRequestDto.getType(),
            animalRequestDto.getName(),
            animalRequestDto.getYearOfBirth(),
            owner);
  }
}
