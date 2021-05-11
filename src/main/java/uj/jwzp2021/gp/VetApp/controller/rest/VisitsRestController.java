package uj.jwzp2021.gp.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.jwzp2021.gp.VetApp.model.dto.VisitMapper;
import uj.jwzp2021.gp.VetApp.model.dto.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.VisitResponseDto;
import uj.jwzp2021.gp.VetApp.model.dto.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.controller.rest.hateoas.VisitRepresentation;
import uj.jwzp2021.gp.VetApp.model.dto.VisitRequest;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.service.VisitService;
import uj.jwzp2021.gp.VetApp.util.VisitCreationError;
import uj.jwzp2021.gp.VetApp.util.VisitLookupError;
import uj.jwzp2021.gp.VetApp.util.VisitUpdateError;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/visits", produces = MediaType.APPLICATION_JSON_VALUE)
public class
VisitsRestController {

  private final VisitService visitsService;

  @Autowired
  public VisitsRestController(VisitService visitsService) {
    this.visitsService = visitsService;
  }

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVisit(@PathVariable int id) {
    var result = visitsService.getVisitById(id);
    return result.map(this::visitLookupErrorToResponse, this::dtoToResponse);
  }

  @GetMapping
  public ResponseEntity<?> getAllVisits() {
//    return visitsService.getAllVisits();
    var visits = visitsService.getAllVisits();
    return ResponseEntity.ok(visits.stream().map(VisitMapper::toVisitResponseDto).collect(Collectors.toList()));
  }

  @PostMapping()
  public ResponseEntity<?> createVisit(@RequestBody VisitRequestDto visitReq) {
    var result = visitsService.createVisit(visitReq);
    return result.map(this::visitCreationErrorToResponse, this::visitToResponse);
  }

  @DeleteMapping(path = "/{id}")
  void delete(@PathVariable int id) {
    visitsService.delete(id);
  }

  @PatchMapping(path = "/{id}")
  ResponseEntity<?> update(@PathVariable int id, @RequestBody VisitUpdateRequestDto visitUpdateRequestDto) {
    var result = visitsService.updateVisit(id, visitUpdateRequestDto);
    return result.map(this::visitUpdateErrorToResponse, this::visitToResponse);
  }

  private ResponseEntity<?> visitToResponse(Visit visit) {
    return ResponseEntity.status(HttpStatus.CREATED).body(VisitMapper.toVisitResponseDto(visit));
  }

  private ResponseEntity<?> dtoToResponse(VisitResponseDto visitResponseDto) {
    return ResponseEntity.status(HttpStatus.OK).body(visitResponseDto);
  }

  private ResponseEntity<?> visitCreationErrorToResponse(VisitCreationError result) {
    switch (result) {
      case STARTS_IN_PAST:
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(RestUtil.response("Can not book a visit in the past."));
      case TOO_SOON:
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(RestUtil.response("Booking for less than an hour in the future is prohibited."));
      case OVERLAP:
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(RestUtil.response("Overlapping with other visit."));
      case REPOSITORY_PROBLEM:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestUtil.response("Problem with server, please try again later."));
      case CLIENT_NOT_EXISTS:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(RestUtil.response("Client with provided id not exists."));
      case ANIMAL_NOT_EXISTS:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(RestUtil.response("Animal with provided id not exists."));
      case VET_NOT_EXISTS:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(RestUtil.response("Vet with provided id not exists."));
      default:
        return ResponseEntity.badRequest().body(RestUtil.response("Unknown error."));
    }
  }

  private ResponseEntity<?> visitUpdateErrorToResponse(VisitUpdateError result) {
    switch (result) {
      case VISIT_NOT_FOUND:
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(RestUtil.response("Visit with such id was not found."));
      case ILLEGAL_FIELD:
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(RestUtil.response("You can modify only status and description."));
      case ILLEGAL_VALUE:
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(
                RestUtil.response(
                    "You can set status only to FINISHED, CANCELLED and NOT_APPEARED values."));
      case REPOSITORY_PROBLEM:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(RestUtil.response("Problem with server, please try again later."));
      default:
        return ResponseEntity.badRequest().body(RestUtil.response("Unknown error."));
    }
  }

  @GetMapping(value = "/hateoas", produces = "application/hal+json")
  public List<VisitRepresentation> getAllHateoas() {
    var visits = visitsService.getAllVisits();
    return visits.stream().map(this::represent).collect(Collectors.toList());
  }

  private VisitRepresentation represent(Visit v) {
    Link selfLink = linkTo(methodOn(VisitsRestController.class).getVisitById(v.getId())).withSelfRel();
    var representation = VisitRepresentation.fromVisit(v);
    representation.add(selfLink);
    return representation;
  }

  private ResponseEntity<?> visitLookupErrorToResponse(VisitLookupError result) {
    switch (result) {
      case NOT_FOUND:
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(RestUtil.response("Visit with such id was not found."));
      default:
        return ResponseEntity.badRequest().body(RestUtil.response("Unknown error."));
    }
  }

}
