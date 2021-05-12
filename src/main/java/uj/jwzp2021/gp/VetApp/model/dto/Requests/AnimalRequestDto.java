package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@Data
public class AnimalRequestDto {
  private final AnimalType type;
  private final String name;
  private final int yearOfBirth;
  private final int ownerId;
}
