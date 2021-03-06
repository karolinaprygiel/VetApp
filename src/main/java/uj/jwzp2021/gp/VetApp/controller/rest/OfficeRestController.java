package uj.jwzp2021.gp.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.OfficeRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.OfficeService;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequestMapping("api/offices")
@RestController
public class OfficeRestController {

  private final OfficeService officeService;

  @Autowired
  public OfficeRestController(OfficeService officeService) {
    this.officeService = officeService;
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<?> getOffice(@PathVariable int id) {
    log.info("Received GET request for api/offices/" + id);
    var officeRepresentation = representFull(officeService.getOfficeById(id));
    log.info("Returning httpStatus=200. Office with id=" + id + " was found.") ;
    return ResponseEntity.ok(officeRepresentation);
  }

  @GetMapping
  public ResponseEntity<?> getAllOffices() {
    log.info("Received GET request for api/offices/");
    var officeRepresentations = officeService.getAll().stream().map(this::representBrief).collect(Collectors.toList());
    log.info("Returning httpStatus=200, returning all offices.");
    return ResponseEntity.ok(officeRepresentations);
  }

  @PostMapping
  public ResponseEntity<?> createOffice(@RequestBody OfficeRequestDto officeRequestDto) {
    log.info("Received POST request for api/offices with: " + officeRequestDto);
    var officeRepresentation = representFull(officeService.createOffice(officeRequestDto));
    log.info("Returning httpStatus=201. Office for request" + officeRequestDto+ " created successfully");
    return ResponseEntity.status(HttpStatus.CREATED).body(officeRepresentation);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteOffice(@PathVariable int id) {
    log.info("Received DELETE request for api/offices/" + id);
    var officeRepresentation = representFull(officeService.deleteOffice(id));
    log.info("Returning httpStatus=200. Office with id=" + id +" deleted successfully");
    return ResponseEntity.ok(officeRepresentation);
  }

  private OfficeRepresentation representBrief(Office o) {
    log.debug("Creating Brief Office representation");
    var representation = OfficeRepresentation.fromOffice(o);
    representation.add(
        linkTo(methodOn(OfficeRestController.class).getOffice(o.getId())).withSelfRel());
    return representation;
  }

  private OfficeRepresentation representFull(Office o) {
    log.debug("Creating Full Office representation");
    var representation = OfficeRepresentation.fromOffice(o);
    representation.add(
        linkTo(methodOn(OfficeRestController.class).getOffice(o.getId())).withSelfRel());
    representation.add(
        o.getVisits().stream()
            .map(Visit::getId)
            .map(
                (v) ->
                    linkTo(methodOn(VisitsRestController.class).getVisit(v)).withRel("visits"))
            .collect(Collectors.toList()));
    return representation;
  }
}
