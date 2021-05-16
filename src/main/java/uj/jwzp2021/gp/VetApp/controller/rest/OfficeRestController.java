package uj.jwzp2021.gp.VetApp.controller.rest;

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
    return ResponseEntity.ok(representFull(officeService.getOfficeById(id)));
  }

  @GetMapping
  public ResponseEntity<?> getAllOffices() {
    return ResponseEntity.ok(
        officeService.getAll().stream().map(this::representFull).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<?> createOffice(@RequestBody OfficeRequestDto officeRequestDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(representFull(officeService.createOffice(officeRequestDto)));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteOffice(@PathVariable int id) {
    return ResponseEntity.ok(representFull(officeService.deleteOffice(id)));
  }

  private OfficeRepresentation representBrief(Office o) {
    var representation = OfficeRepresentation.fromOffice(o);
    representation.add(
        linkTo(methodOn(OfficeRestController.class).getOffice(o.getId())).withSelfRel());
    return representation;
  }

  private OfficeRepresentation representFull(Office o) {
    var representation = OfficeRepresentation.fromOffice(o);
    representation.add(
        linkTo(methodOn(OfficeRestController.class).getOffice(o.getId())).withSelfRel());
    representation.add(
        o.getVisits().stream()
            .map(Visit::getId)
            .map(
                (v) ->
                    linkTo(methodOn(VisitsRestController.class).getVisit(v)).withRel("oneOfVisits"))
            .collect(Collectors.toList()));
    return representation;
  }
}
