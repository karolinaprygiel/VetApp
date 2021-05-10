package uj.jwzp2021.gp.VetApp.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.dto.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.service.ClientService;
import uj.jwzp2021.gp.VetApp.util.ClientCreationError;

import java.util.stream.Collectors;

@RequestMapping("api/clients")
@RestController
public class ClientRestController {

    private final ClientService clientService;

    @Autowired
    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getClient(@PathVariable int id) {
        return clientService.getClientById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



    @GetMapping
    public ResponseEntity<?> getAllClients() {
//      var clients = clientService.getAll();
//      return ResponseEntity.ok(clients);
      return ResponseEntity.ok(clientService.getAll().stream().map(ClientMapper::toClientResponseDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?>createClient(@RequestBody ClientRequestDto clientRequestDto){
        var result = clientService.createClient(clientRequestDto);
        return result.map(this::clientCreationResultBadRequest, this::clientToResult);
    }

    @DeleteMapping(path = "/{id}")
    void deleteClient(@PathVariable int id){
        clientService.deleteClient(id);
    }

    private ResponseEntity<?> clientToResult(Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    private ResponseEntity<?> clientCreationResultBadRequest(ClientCreationError result){
        return ResponseEntity.badRequest().body(RestUtil.response("Unknown error."));
    }

}
