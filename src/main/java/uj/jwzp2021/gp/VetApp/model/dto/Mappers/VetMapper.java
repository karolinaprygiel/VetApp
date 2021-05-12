package uj.jwzp2021.gp.VetApp.model.dto.Mappers;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;

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
