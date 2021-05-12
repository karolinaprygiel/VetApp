package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.Mappers.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.AnimalResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.repository.AnimalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

  private final AnimalRepository animalRepository;
  private final ClientService clientService;

  @Autowired
  public AnimalService(AnimalRepository animalRepository, ClientService clientService) {
    this.animalRepository = animalRepository;
    this.clientService = clientService;
  }

  public Animal getRawAnimalById(int id) {
    var animal = animalRepository.findById(id);
    return animal.orElseThrow(() -> {
      throw new AnimalNotFoundException("Animal with id:" + id + " not found");
    });
  }

  public AnimalResponseDto getAnimalById(int id) {
    return AnimalMapper.toAnimalResponseDto(getRawAnimalById(id));
  }

  public List<AnimalResponseDto> getAllAnimals() {
    return animalRepository.findAll().stream().map(AnimalMapper::toAnimalResponseDto).collect(Collectors.toList());
  }

  public AnimalResponseDto deleteAnimal(int id) {
    var animal = getRawAnimalById(id);
    animalRepository.delete(animal);
    return AnimalMapper.toAnimalResponseDto(animal);
  }

  public AnimalResponseDto createAnimal(AnimalRequestDto animalRequestDto) {
    var owner = clientService.getRawClientById(animalRequestDto.getOwnerId());
    var animal = animalRepository.save(AnimalMapper.toAnimal(animalRequestDto, owner));
    return AnimalMapper.toAnimalResponseDto(animal);

  }
}
