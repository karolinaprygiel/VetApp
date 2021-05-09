package uj.jwzp2021.gp.VetApp.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.VisitRequest;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.service.ClientService;

import java.util.List;

@RequestMapping("api/clients")
@RestController
public class ClientRestController {

    private final ClientService clientService;

    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getClient(@PathVariable int id) {
        return clientService.getClientById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @DeleteMapping(path = "/{id}")
    void deleteClient(@PathVariable int id){
        clientService.deleteClient(id);
    }

}
