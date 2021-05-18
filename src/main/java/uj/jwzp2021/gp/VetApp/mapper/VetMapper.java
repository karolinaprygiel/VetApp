package uj.jwzp2021.gp.VetApp.mapper;

import org.springframework.stereotype.Component;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;

import java.time.LocalTime;

@Component
public class VetMapper {

  public  Vet toVet(VetRequestDto vetRequestDto) {
    return new Vet(
        -1,
        vetRequestDto.getName(),
        vetRequestDto.getSurname(),
        LocalTime.parse(vetRequestDto.getShiftStart()),
        vetRequestDto.getWorkingTime());
  }
}
