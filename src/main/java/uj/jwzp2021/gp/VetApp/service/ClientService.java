package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.model.dto.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.dto.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.ClientResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;
import uj.jwzp2021.gp.VetApp.util.ClientCreationError;
import uj.jwzp2021.gp.VetApp.util.OperationResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
  private final ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public List<ClientResponseDto> getAll() {
    var clients =  clientRepository.findAll();
    var clientDtis = clients.stream().map(ClientMapper::toClientResponseDto).collect(Collectors.toList());
    return clientDtis;
  }

  public Optional<Client> getClientById(int id) {
    var client = clientRepository.findById(id);
    return client;
  }

  public void deleteClient(int id) {
    var client = clientRepository.findById(id);
      client.ifPresent(value -> clientRepository.deleteById(value.getId()));
  }

  public OperationResult<ClientCreationError, Client> createClient(ClientRequestDto clientRequestDto) {
    Client c =
        clientRepository.save(
            Client.newClient(clientRequestDto.getName(), clientRequestDto.getSurname()));
    return OperationResult.success(c);
  }
}
