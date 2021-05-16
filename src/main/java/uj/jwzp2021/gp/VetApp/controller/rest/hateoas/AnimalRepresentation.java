package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class AnimalRepresentation extends RepresentationModel<AnimalRepresentation> {

  private final int id;
  private final String name;
  private final AnimalType animalType;
  private final int yearOfBirth;
  private final int ownerId;

  public static AnimalRepresentation fromAnimal(Animal animal) {
    log.debug("Creating new AnimalRepresentation");
    return new AnimalRepresentation(
        animal.getId(),
        animal.getName(),
        animal.getType(),
        animal.getYearOfBirth(),
        animal.getOwner().getId());
  }
}
