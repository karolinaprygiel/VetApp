package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.AnimalResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

public class AnimalMapper {
  public static AnimalResponseDto toAnimalResponseDto(Animal animal) {
    return new AnimalResponseDto(
        animal.getId(),
        animal.getType(),
        animal.getName(),
        animal.getYearOfBirth(),
        animal.getOwner().getId());
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
