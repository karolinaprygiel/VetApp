package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;

@Data
@AllArgsConstructor
public class VetRepresentation extends RepresentationModel<VetRepresentation> {
  private final int id;
  private final String name;
  private final String surname;
  private final String shiftStart;
  private final String shiftEnd;

  public static VetRepresentation fromVetResponseDto(VetResponseDto vetResponseDto) {
    return new VetRepresentation(
        vetResponseDto.getId(),
        vetResponseDto.getName(),
        vetResponseDto.getSurname(),
        vetResponseDto.getShiftStart(),
        vetResponseDto.getShiftEnd());
  }
}
