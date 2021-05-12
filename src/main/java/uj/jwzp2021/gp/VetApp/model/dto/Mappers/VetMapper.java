package uj.jwzp2021.gp.VetApp.model.dto.Mappers;

import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;

import java.time.LocalTime;

public class VetMapper {
  public static VetResponseDto toVetResponseDto(Vet vet){
    return new VetResponseDto(
        vet.getId(),
        vet.getName(),
        vet.getSurname(),
        vet.getShiftStart().toString(),
        vet.getShiftEnd().toString()
    );
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
