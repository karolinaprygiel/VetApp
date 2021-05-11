package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.*;

public class VisitMapper {
  public static Visit toVisit(
      VisitRequestDto visitRequestDto, Animal animal, Client client, Vet vet) {
    return new Visit(
        -1,
        visitRequestDto.getStartTime(),
        visitRequestDto.getDuration(),
        Status.PLANNED,
        visitRequestDto.getPrice(),
        animal,
        client,
        vet,
        "");
  }

  public static VisitResponseDto toVisitResponseDto(
      Visit visit) {
    return new VisitResponseDto(
        visit.getId(),
        visit.getStartTime(),
        visit.getDuration(),
        visit.getStatus().toString(),
        visit.getPrice(),
        visit.getAnimal().getId(),
        visit.getClient().getId(),
        visit.getVet().getId(),
        visit.getDescription()
    );
  }
}
