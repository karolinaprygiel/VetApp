package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.*;

public class VisitMapper {
  public static Visit toVisit(
      VisitRequestDto visitRequestDto, Animal animal, Client client, Vet vet, Office office) {
    return new Visit(
        -1,
        visitRequestDto.getStartTime(),
        visitRequestDto.getDuration(),
        VisitStatus.PLANNED,
        visitRequestDto.getPrice(),
        animal,
        client,
        vet,
        office,
        "");
  }

  public static VisitResponseDto toVisitResponseDto(Visit visit) {
    var office = visit.getOffice();

    return new VisitResponseDto(
        visit.getId(),
        visit.getStartTime(),
        visit.getDuration(),
        visit.getVisitStatus(),
        visit.getPrice(),
        visit.getAnimal().getId(),
        visit.getClient().getId(),
        visit.getVet().getId(),
        office != null ? office.getId() : -1,
        visit.getDescription());
  }
}
