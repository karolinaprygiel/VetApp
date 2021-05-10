package uj.jwzp2021.gp.VetApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@Data
@AllArgsConstructor
public class AnimalResponseDto {
  private int animalId;
  private String animalType;
  private String name;
  private int yearOfBirth;
  private int clientId;
  private String clientName;
  private String clientSurname;
}
