package uj.jwzp2021.gp.VetApp.mapper;

import org.springframework.stereotype.Component;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;

@Component
public class AnimalMapper {

  public Animal toAnimal(AnimalRequestDto animalRequestDto, Client owner) {
    return new Animal(
        -1,
        animalRequestDto.getType(),
        animalRequestDto.getName(),
        animalRequestDto.getYearOfBirth(),
        owner);
  }


}
