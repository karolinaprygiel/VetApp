package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class AnimalRepresentation extends RepresentationModel<AnimalRepresentation> {
  private final int id;
  private final String name;
  private final AnimalType animalType;
  private final int yearOfBirth;
  private final int ownerId;

  public static AnimalRepresentation fromAnimal(Animal animal) {
    return new AnimalRepresentation(
        animal.getId(),
        animal.getName(),
        animal.getType(),
        animal.getYearOfBirth(),
        animal.getOwner().getId());
  }
}
