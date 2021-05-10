package uj.jwzp2021.gp.VetApp.model.dto;

import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@Data
public class AnimalResponseDto {
  private int animalId;
  private AnimalType animalType;
  private String name;
  private int yearOfBirth;
  private int clientId;
}
