package uj.jwzp2021.gp.VetApp.mapper;

import org.springframework.stereotype.Component;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

@Component
public class ClientMapper {

  public Client toClient(ClientRequestDto clientRequestDto) {
    return new Client(-1, clientRequestDto.getName(), clientRequestDto.getSurname());
  }
}
