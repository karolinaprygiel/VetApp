package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class ClientRepresentation extends RepresentationModel<ClientRepresentation> {
  private final int id;
  private final String name;
  private final String surname;

  public static ClientRepresentation fromClient(Client clientResponseDto) {
    log.debug("Creating new ClientRepresentation");
    return new ClientRepresentation(
        clientResponseDto.getId(), clientResponseDto.getName(), clientResponseDto.getSurname());
  }
}
