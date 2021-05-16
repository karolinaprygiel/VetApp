package uj.jwzp2021.gp.VetApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VetService {
  private final VetRepository vetRepository;

  @Autowired
  public VetService(VetRepository vetRepository) {
    this.vetRepository = vetRepository;
  }

  public Vet getVetById(int id) {
    var vet = vetRepository.findById(id);
    return vet.orElseThrow(
        () -> {
          throw new VetNotFoundException("Vet with id:" + id + " not found.");
        });
  }

  public List<Vet> getAll() {
    return vetRepository.findAll();
  }

  public Vet createVet(VetRequestDto vetRequestDto) {
    return vetRepository.save(VetMapper.toVet(vetRequestDto));
  }

  public Vet deleteVet(int id) {
    var vet = getVetById(id);
    vetRepository.delete(vet);
    return vet;
  }

  public boolean vetAvailable(LocalDateTime startTime, Duration duration, int vetId) {
    var vet = getVetById(vetId);
    return startTime.plus(duration).toLocalTime().isBefore(vet.getShiftEnd().plusSeconds(1))
        && startTime.toLocalTime().isAfter(vet.getShiftStart().minusSeconds(1));
  }
}
