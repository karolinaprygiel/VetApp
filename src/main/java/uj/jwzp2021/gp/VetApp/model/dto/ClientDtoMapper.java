package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.Client;

public class ClientDtoMapper {
  public static Client toClient(ClientRequestDto clientRequestDto) {
    return new Client(-1, clientRequestDto.getName(), clientRequestDto.getSurname());
  }
}
