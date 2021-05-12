package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.ClientResponseDto;

@Data
@AllArgsConstructor
public class VetRepresentation extends RepresentationModel<ClientRepresentation> {
  private final int id;
  private final String name;
  private final String surname;
  private final String shiftStart;
  private final String shiftEnd;

  public static ClientRepresentation fromClientResponseDto(ClientResponseDto clientResponseDto) {
    return new ClientRepresentation(
        clientResponseDto.getId(),
        clientResponseDto.getName(),
        clientResponseDto.getSurname()
//        clientResponseDto.
    );
  }
}