package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.ClientRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.ClientResponseDto;
import uj.jwzp2021.gp.VetApp.service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    return ResponseEntity.ok(clientService.getClientById(id));
  }

  @GetMapping
  public ResponseEntity<?> getAllClients() {
    return ResponseEntity.ok(clientService.getAll());
  }

  @PostMapping
  public ResponseEntity<?> createClient(@RequestBody ClientRequestDto clientRequestDto) {
    var client = clientService.createClient(clientRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(client);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteClient(@PathVariable int id) {
    var client = clientService.deleteClient(id);
    return ResponseEntity.ok(client);
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<ClientRepresentation> getAllHateoas() {
    var clients = clientService.getAll();
    return clients.stream().map(this::represent).collect(Collectors.toList());
  }

  private ClientRepresentation represent(ClientResponseDto c) {
    var representation = ClientRepresentation.fromClientResponseDto(c);
    representation.add(
        linkTo(methodOn(VisitsRestController.class).getVisit(c.getId())).withSelfRel());
    representation.add(
        c.getAnimalIds().stream()
            .map(
                (id) ->
                    linkTo(methodOn(AnimalRestController.class).getAnimal(id)).withRel("oneOfPets"))
            .collect(Collectors.toList()));
    return representation;
  }
}
