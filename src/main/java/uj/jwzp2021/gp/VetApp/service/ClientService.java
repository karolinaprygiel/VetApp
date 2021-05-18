package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.client.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClientService {
  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;

  @Autowired
  public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
    this.clientRepository = clientRepository;
    this.clientMapper = clientMapper;
  }

  public Client getClientById(int id) {
    log.info("Looking up client with id=" + id);
    var client = clientRepository.findById(id);
    return client.orElseThrow(
        () -> {
          throw new ClientNotFoundException("Client with id:" + id + " not found");
        });
  }

  public List<Client> getAll() {
    log.info("Looking up all clients");
    return new ArrayList<>(clientRepository.findAll());
  }

  public Client deleteClient(int id) {
    var client = getClientById(id);
    try{
    clientRepository.delete(client);
    }catch (DataAccessException ex){
      log.error("Repository error while deleting client with id=" + id);
      throw ex;
    }
    log.info("Client with id=" + id + " deleted successfully");
    return client;
  }

  public Client createClient(ClientRequestDto clientRequestDto) {
    Client client;
    try{
      client = clientRepository.save(clientMapper.toClient(clientRequestDto));
    }catch (DataAccessException ex){
      log.error("Repository problem while saving client for request: " + clientRequestDto);
      throw ex;
    }
    log.info("Client for request: " + clientRequestDto + " created successfully");
    return client;
    }
}
