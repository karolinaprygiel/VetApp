package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.repository.AnimalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

  private final AnimalRepository animalRepository;
  private final ClientService clientService;

  @Autowired
  public AnimalService(AnimalRepository animalRepository, ClientService clientService) {
    this.animalRepository = animalRepository;
    this.clientService = clientService;
  }

  public Animal getAnimalById(int id) {
    var animal = animalRepository.findById(id);
    return animal.orElseThrow(() -> {
      throw new AnimalNotFoundException("Animal with id " + id + " not found");
    });


  }

  public List<Animal> getAllAnimals() {
    return animalRepository.findAll();
  }

  public Optional<Animal> deleteAnimal(int id) {
    var animal = animalRepository.findById(id);
    if (animal.isPresent()) {
      animalRepository.deleteById(animal.get().getId());
    }
    return animal;
  }

  public Optional<Animal> createAnimal(AnimalRequestDto animalRequestDto) {
    Animal animal = null;
    var owner = clientService.getClientById(animalRequestDto.getOwnerId());
//    if (owner.isPresent()) {
//      animal = animalRepository.save(AnimalMapper.toAnimal(animalRequestDto, owner.get()));
//    }
    animal = animalRepository.save(AnimalMapper.toAnimal(animalRequestDto, owner));
    return Optional.ofNullable(animal);
  }
}
