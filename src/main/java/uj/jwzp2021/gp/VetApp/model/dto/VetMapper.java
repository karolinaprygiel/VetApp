package uj.jwzp2021.gp.VetApp.model.dto;

import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;

public class VetMapper {
  public static VetResponseDto toVetResponseDto(Vet vet){
    return new VetResponseDto(
        vet.getId(),
        vet.getName(),
        vet.getSurname(),
        vet.getShiftStart(),
        vet.getShiftEnd()
    );
  }

  public static Vet toVet(VetRequestDto vetRequestDto) {
    return new Vet(
            -1,
            vetRequestDto.getName(),
            vetRequestDto.getSurname(),
            vetRequestDto.getShiftStart(),
            vetRequestDto.getShiftEnd());
  }
}
