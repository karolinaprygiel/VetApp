package uj.jwzp2021.gp.VetApp.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.exception.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalMapper;
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
        return  ResponseEntity.ok(ClientMapper.toClientResponseDto(clientService.getClientById(id)));
    }

    @GetMapping
    public ResponseEntity<?> getAllClients() {
      return ResponseEntity.ok(clientService.getAll().stream().map(ClientMapper::toClientResponseDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?>createClient(@RequestBody ClientRequestDto clientRequestDto){
        var client = clientService.createClient(clientRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toClientResponseDto(client));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable int id){
        var client = clientService.deleteClient(id);
        return ResponseEntity.accepted().body(ClientMapper.toClientResponseDto(client));
    }
}
