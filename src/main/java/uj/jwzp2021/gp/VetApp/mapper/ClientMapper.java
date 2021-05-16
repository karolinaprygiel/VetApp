package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

public class ClientMapper {

  public static Client toClient(ClientRequestDto clientRequestDto) {
    return new Client(-1, clientRequestDto.getName(), clientRequestDto.getSurname());
  }
}
