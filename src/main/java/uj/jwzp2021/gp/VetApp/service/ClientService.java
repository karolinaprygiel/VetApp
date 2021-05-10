package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.model.dto.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;
import uj.jwzp2021.gp.VetApp.util.ClientCreationError;
import uj.jwzp2021.gp.VetApp.util.OperationResult;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
  private final ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public List<Client> getAllClients() {
    return clientRepository.findAll();
  }

  public Optional<Client> getClientById(int id) {
    return clientRepository.findById(id);
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
