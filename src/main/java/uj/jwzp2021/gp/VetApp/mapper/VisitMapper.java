package uj.jwzp2021.gp.VetApp.mapper;

import org.springframework.stereotype.Component;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.*;

@Component
public class VisitMapper {
  public Visit toVisit(
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


}
