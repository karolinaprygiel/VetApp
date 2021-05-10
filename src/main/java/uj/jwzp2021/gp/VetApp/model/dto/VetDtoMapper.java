package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.Vet;

public class VetDtoMapper {
  public static Vet toVet(VetRequestDto vetRequestDto) {
    return new Vet(
        -1,
        vetRequestDto.getName(),
        vetRequestDto.getSurname(),
        vetRequestDto.getShiftStart(),
        vetRequestDto.getShiftEnd());
  }
}
