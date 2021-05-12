package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.client.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.Mappers.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.ClientResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
  private final ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public Client getRawClientById(int id) {
    var client = clientRepository.findById(id);
    return client.orElseThrow(() -> {
      throw new ClientNotFoundException("Client with id:" + id + " not found.");
    });
  }

  public ClientResponseDto getClientById(int id) {
    return ClientMapper.toClientResponseDto(getRawClientById(id));

  }

  public List<ClientResponseDto> getAll() {
    return clientRepository.findAll().stream()
        .map(ClientMapper::toClientResponseDto)
        .collect(Collectors.toList());
  }

  public ClientResponseDto deleteClient(int id) {
    var client = getRawClientById(id);
    clientRepository.delete(client);
    return ClientMapper.toClientResponseDto(client);
}
  public ClientResponseDto createClient(ClientRequestDto clientRequestDto) {
    var client = clientRepository.save(ClientMapper.toClient(clientRequestDto));
    return ClientMapper.toClientResponseDto(client);
  }
}
