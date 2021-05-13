package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.ClientResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

import java.util.stream.Collectors;

public class ClientMapper {
  public static ClientResponseDto toClientResponseDto(Client client) {
    return new ClientResponseDto(
        client.getId(),
        client.getName(),
        client.getSurname(),
        client.getAnimals().stream().map(Animal::getId).collect(Collectors.toList()));
  }

  public static Client toClient(ClientRequestDto clientRequestDto) {
    return new Client(-1, clientRequestDto.getName(), clientRequestDto.getSurname());
  }
}
