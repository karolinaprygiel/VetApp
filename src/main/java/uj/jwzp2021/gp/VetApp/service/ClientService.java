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

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
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
    log.debug("Looking up all clients");
    return new ArrayList<>(clientRepository.findAll());
  }

  public Client deleteClient(int id) {
    log.debug("Deleting client with id=" + id);
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
    log.debug("Creating client for: " + clientRequestDto);
    Client client;
    try{
      client = clientRepository.save(ClientMapper.toClient(clientRequestDto));
    }catch (DataAccessException ex){
      log.error("Repository problem while saving client for request: " + clientRequestDto);
      throw ex;
    }
    log.info("Client for request: " + clientRequestDto + " created successfully");
    return client;
    }
}
