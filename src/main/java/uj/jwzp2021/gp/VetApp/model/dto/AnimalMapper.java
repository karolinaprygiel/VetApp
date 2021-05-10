package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.Animal;

public class AnimalMapper {
  public static AnimalResponseDto toAnimalResponseDto(Animal animal) {
    return new AnimalResponseDto(
        animal.getId(),
        animal.getType(),
        animal.getName(),
        animal.getYearOfBirth(),
        animal.getOwner().getId(),
        animal.getOwner().getName(),
        animal.getOwner().getSurname());
  }
}
