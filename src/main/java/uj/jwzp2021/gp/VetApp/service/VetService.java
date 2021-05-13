package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VetResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VetService {
  private final VetRepository vetRepository;

  @Autowired
  public VetService (VetRepository vetRepository){
    this.vetRepository = vetRepository;
  }

  Vet getRawVetById(int id) {
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

  public boolean vetAvailable(LocalDateTime startTime, Duration duration, int vetId) {
    var vet = getRawVetById(vetId);
    return startTime.plus(duration).toLocalTime().isBefore(vet.getShiftEnd().plusSeconds(1)) &&
        startTime.toLocalTime().isAfter(vet.getShiftStart().minusSeconds(1));
  }
}
