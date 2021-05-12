package uj.jwzp2021.gp.VetApp.model.dto.Mappers;


import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.ClientResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

public class ClientMapper {
  public static ClientResponseDto toClientResponseDto(Client client) {
    return new ClientResponseDto(client.getId(), client.getName(), client.getSurname());
  }

  public static Client toClient(ClientRequestDto clientRequestDto) {
    return new Client(-1, clientRequestDto.getName(), clientRequestDto.getSurname());
  }
}
