package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class VetRepresentation extends RepresentationModel<VetRepresentation> {
  private final int id;
  private final String name;
  private final String surname;
  private final String shiftStart;
  private final String workingTime;

  public static VetRepresentation fromVet(Vet vet) {
    log.debug("Creating new VetRepresentation");
    return new VetRepresentation(
        vet.getId(),
        vet.getName(),
        vet.getSurname(),
        vet.getShiftStart().toString(),
        vet.getWorkingTime().toString());
  }
}
