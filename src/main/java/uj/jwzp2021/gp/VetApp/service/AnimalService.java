package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.repository.AnimalRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AnimalService {

  private final AnimalRepository animalRepository;
  private final ClientService clientService;

  @Autowired
  public AnimalService(AnimalRepository animalRepository, ClientService clientService) {
    this.animalRepository = animalRepository;
    this.clientService = clientService;
  }

  public Animal getAnimalById(int id) {
    log.debug("Looking up animal with id=" + id);
    var animal = animalRepository.findById(id);
    return animal.orElseThrow(
        () -> {
          log.debug("Not found animal with id=" + id);
          throw new AnimalNotFoundException("Animal with id:" + id + " not found.");
        });
  }

  public List<Animal> getAllAnimals() {
    log.debug("Returning all animals");
    return new ArrayList<>(animalRepository.findAll());
  }

  public Animal deleteAnimal(int id) {
    log.debug("Deleting animal with id=" + id);
    var animal = getAnimalById(id);
    animalRepository.delete(animal);
    return animal;
  }

  public Animal createAnimal(AnimalRequestDto animalRequestDto) {
    log.debug("Creating animal for: " + animalRequestDto);
    var owner = clientService.getClientById(animalRequestDto.getOwnerId());
    return animalRepository.save(AnimalMapper.toAnimal(animalRequestDto, owner));
  }
}
