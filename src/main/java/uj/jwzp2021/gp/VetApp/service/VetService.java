package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotFoundException;
import uj.jwzp2021.gp.VetApp.model.dto.Mappers.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VetService {
  private final VetRepository vetRepository;

  @Autowired
  public VetService (VetRepository vetRepository){
    this.vetRepository = vetRepository;
  }

  public Vet getRawVetById(int id) {
    var vet = vetRepository.findById(id);
    return vet.orElseThrow(()-> {throw new VetNotFoundException("Vet with id:" + id +" not found.");
    });
  }

  public VetResponseDto getVetById(int id){
    return VetMapper.toVetResponseDto(getRawVetById(id));
  }

  public List<VetResponseDto> getAll() {
      return vetRepository.findAll().stream()
          .map(VetMapper::toVetResponseDto)
          .collect(Collectors.toList());
  }

  public VetResponseDto createVet(VetRequestDto vetRequestDto) {
    var vet = vetRepository.save(VetMapper.toVet(vetRequestDto));
    return VetMapper.toVetResponseDto(vet);
  }

  public VetResponseDto deleteVet(int id) {
    var vet = getRawVetById(id);
    vetRepository.delete(vet);
    return VetMapper.toVetResponseDto(vet);
  }
}
