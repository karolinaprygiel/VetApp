package uj.jwzp2021.gp.VetApp.mapper;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;

import java.time.LocalTime;
import java.util.stream.Collectors;

public class VetMapper {
  public static VetResponseDto toVetResponseDto(Vet vet) {
    return new VetResponseDto(
        vet.getId(),
        vet.getName(),
        vet.getSurname(),
        vet.getShiftStart().toString(),
        vet.getShiftEnd().toString(),
        vet.getVisits().stream().map(Visit::getId).collect(Collectors.toList()));
  }

  public static Vet toVet(VetRequestDto vetRequestDto) {
    return new Vet(
        -1,
        vetRequestDto.getName(),
        vetRequestDto.getSurname(),
        LocalTime.parse(vetRequestDto.getShiftStart()),
        LocalTime.parse(vetRequestDto.getShiftEnd()));
  }
}
