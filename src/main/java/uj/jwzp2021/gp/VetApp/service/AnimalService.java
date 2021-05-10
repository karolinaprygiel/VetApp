package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.model.dto.AnimalRequest;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.AnimalRepository;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;
import uj.jwzp2021.gp.VetApp.util.OperationResult;

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

    public Optional<Animal> getAnimalById(int id) {
    return animalRepository.findById(id);
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

  public Optional<Animal> createAnimal(AnimalRequest animalRequest) {
      Animal animal = null;
      var owner = clientService.getClientById(animalRequest.getOwnerId());
      if(owner.isPresent()){
        //Animal.newAnimal(animalRequest.getType(), animalRequest.getName(), animalRequest.getYearOfBirth(), owner.get());
        animal = animalRepository.save(Animal.newAnimal(animalRequest.getType(), animalRequest.getName(), animalRequest.getYearOfBirth(), owner.get()));
      }
      return Optional.ofNullable(animal);

  }
}
