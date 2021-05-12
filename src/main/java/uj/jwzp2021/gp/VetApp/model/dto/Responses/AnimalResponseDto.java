package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@Data
@AllArgsConstructor
public class AnimalResponseDto {
  private int id;
  private AnimalType type;
  private String name;
  private int yearOfBirth;
  private int ownerId;
}
