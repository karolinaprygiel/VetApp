package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Office;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class OfficeRepresentation extends RepresentationModel<OfficeRepresentation> {
  private final int id;
  private final String name;

  public static OfficeRepresentation fromOffice(Office office) {
    return new OfficeRepresentation(office.getId(), office.getName());
  }
}
