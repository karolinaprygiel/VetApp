package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.model.dto.ClientRequest;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;
import uj.jwzp2021.gp.VetApp.util.ClientCreationResult;
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

    public boolean deleteClient(int id) {
        var client = clientRepository.findById(id);
        if (client.isPresent()) {
            clientRepository.deleteById(client.get().getId());
            return true;
        } else {
            return false;
        }
    }

    public OperationResult<ClientCreationResult, Client> createClient(ClientRequest clientRequest) {
        Client c = clientRepository.save(Client.newClient(clientRequest.getName(), clientRequest.getSurname()));
        return OperationResult.success(c);

    }
}
