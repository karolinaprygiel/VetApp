package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.client.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
  private final ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public Client getClientById(int id) {
    var client = clientRepository.findById(id);
    return client.orElseThrow(
        () -> {
          throw new ClientNotFoundException("Client with id:" + id + " not found.");
        });
  }

  public List<Client> getAll() {
    return new ArrayList<>(clientRepository.findAll());
  }

  public Client deleteClient(int id) {
    var client = getClientById(id);
    clientRepository.delete(client);
    return client;
  }

  public Client createClient(ClientRequestDto clientRequestDto) {
    var client = clientRepository.save(ClientMapper.toClient(clientRequestDto));
    return client;
  }
}
