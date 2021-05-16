package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.ClientRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.ClientService;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("api/clients")
@RestController
@Slf4j
public class ClientRestController {

  private final ClientService clientService;

  @Autowired
  public ClientRestController(ClientService clientService) {
    this.clientService = clientService;
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<?> getClient(@PathVariable int id) {
    log.info("Received GET request for /clients/" + id);
    return ResponseEntity.ok(representFull(clientService.getClientById(id)));
  }

  @GetMapping
  public ResponseEntity<?> getAllClients() {
    log.info("Received GET request for /clients");
    return ResponseEntity.ok(
        clientService.getAll().stream().map(this::representBrief).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<?> createClient(@RequestBody ClientRequestDto clientRequestDto) {
    log.info("Received POST request for /clients with " + clientRequestDto);
    var client = clientService.createClient(clientRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(representFull(client));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteClient(@PathVariable int id) {
    log.info("Received DELETE request for /clients/" + id);
    var client = clientService.deleteClient(id);
    return ResponseEntity.ok(representFull(client));
  }

  private ClientRepresentation representBrief(Client c) {
    log.debug("Creating Brief Client representation");
    var representation = ClientRepresentation.fromClient(c);
    representation.add(
        linkTo(methodOn(ClientRestController.class).getClient(c.getId())).withSelfRel());
    return representation;
  }

  private ClientRepresentation representFull(Client c) {
    log.debug("Creating Full Client representation");
    var representation = ClientRepresentation.fromClient(c);
    representation.add(
        linkTo(methodOn(ClientRestController.class).getClient(c.getId())).withSelfRel());
    representation.add(
        c.getAnimals().stream()
            .map(Animal::getId)
            .map(
                (id) ->
                    linkTo(methodOn(AnimalRestController.class).getAnimal(id)).withRel("pets"))
            .collect(Collectors.toList()));
    representation.add(
        c.getVisits().stream()
            .map(Visit::getId)
            .map(
                (visit) ->
                    linkTo(methodOn(VisitsRestController.class).getVisit(visit)).withRel("visits"))
            .collect(Collectors.toList()));
    return representation;
  }
}
