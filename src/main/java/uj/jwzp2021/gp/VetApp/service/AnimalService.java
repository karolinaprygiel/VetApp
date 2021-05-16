package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    log.info("Looking up animal with id=" + id);
    var animal = animalRepository.findById(id);
    return animal.orElseThrow(
        () -> {
          throw new AnimalNotFoundException("Animal with id=" + id + " not found");
        });
  }

  public List<Animal> getAllAnimals() {
    log.debug("Looking up all animals");
    return new ArrayList<>(animalRepository.findAll());
  }

  public Animal deleteAnimal(int id) {
    log.debug("Deleting animal with id=" + id);
    var animal = getAnimalById(id);
    try{
    animalRepository.delete(animal);
    }catch (DataAccessException ex){
      log.error("Repository error while deleting animal with id=" + id);
      throw ex;
    }
    log.info("Animal with id=" + id + " deleted successfully");
    return animal;
  }

  public Animal createAnimal(AnimalRequestDto animalRequestDto) {
    log.info("Creating animal for: " + animalRequestDto);
    Animal animal;
    var owner = clientService.getClientById(animalRequestDto.getOwnerId());
    try{
    animal = animalRepository.save(AnimalMapper.toAnimal(animalRequestDto, owner));

    }catch (DataAccessException ex){
      log.error("Repository problem while saving animal for request: " + animalRequestDto);
      throw ex;
    }
    log.info("Animal for request: " + animalRequestDto + " created successfully");
    return animal;
    }
}
