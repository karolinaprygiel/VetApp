package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.OfficeResponseDto;

@Data
@AllArgsConstructor
public class OfficeRepresentation extends RepresentationModel<OfficeRepresentation> {
  private final int id;
  private final String name;

  public static OfficeRepresentation fromOfficeResponseDto(OfficeResponseDto officeResponseDto) {
    return new OfficeRepresentation(officeResponseDto.getId(), officeResponseDto.getName());
  }
}
