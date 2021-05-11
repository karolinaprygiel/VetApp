package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.model.dto.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.VetResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;
import uj.jwzp2021.gp.VetApp.util.OperationResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class VetService {
    private final VetRepository vetRepository;

    @Autowired
    public VetService (VetRepository vetRepository){
        this.vetRepository = vetRepository;
    }

  public Optional<Vet> getVetById(int id) {
     return vetRepository.findById(id);
  }

  public List<Vet> getAll() {
      return vetRepository.findAll();
  }

  public Vet createVet(VetRequestDto vetRequestDto) {
    // Animal.newAnimal(animalRequest.getType(), animalRequest.getName(),
    // animalRequest.getYearOfBirth(), owner.get());
    return vetRepository.save(VetMapper.toVet(vetRequestDto));
  }


  public Optional<Vet> deleteVet(int id) {
    var vet = vetRepository.findById(id);
    if (vet.isPresent()) {
      vetRepository.deleteById(vet.get().getId());
    }
    return vet;
  }
}
