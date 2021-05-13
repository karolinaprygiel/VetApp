package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.OfficeRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.OfficeResponseDto;
import uj.jwzp2021.gp.VetApp.service.OfficeService;

import java.util.List;
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
    return ResponseEntity.ok(officeService.getOfficeById(id));
  }

  @GetMapping
  public ResponseEntity<?> getAllOffices() {
    return ResponseEntity.ok(officeService.getAll());
  }

  @PostMapping
  public ResponseEntity<?> createOffice(@RequestBody OfficeRequestDto officeRequestDto) {
    var office = officeService.createOffice(officeRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(office);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteOffice(@PathVariable int id) {
    var office = officeService.deleteOffice(id);
    return ResponseEntity.ok(office);
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<OfficeRepresentation> getAllHateoas() {
    var offices = officeService.getAll();
    return offices.stream().map(this::represent).collect(Collectors.toList());
  }

  private OfficeRepresentation represent(OfficeResponseDto o) {
    var representation = OfficeRepresentation.fromOfficeResponseDto(o);
    representation.add(
        linkTo(methodOn(OfficeRestController.class).getOffice(o.getId())).withSelfRel());
    representation.add(
        o.getVisitIds().stream()
            .map((v) -> linkTo(methodOn(VisitsRestController.class).getVisit(v)).withRel("oneOfVisits"))
            .collect(Collectors.toList()));
    return representation;
  }
}
