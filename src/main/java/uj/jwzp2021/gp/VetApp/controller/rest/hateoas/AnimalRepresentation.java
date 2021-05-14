package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.AnimalResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@Data
@AllArgsConstructor
public class AnimalRepresentation extends RepresentationModel<AnimalRepresentation> {
  private final int id;
  private final String name;
  private final AnimalType animalType;
  private final int yearOfBirth;
  private final int ownerId;

  public static AnimalRepresentation fromAnimal(AnimalResponseDto animalResponseDto) {
    return new AnimalRepresentation(
        animalResponseDto.getId(),
        animalResponseDto.getName(),
        animalResponseDto.getType(),
        animalResponseDto.getYearOfBirth(),
        animalResponseDto.getOwnerId());
  }
}
