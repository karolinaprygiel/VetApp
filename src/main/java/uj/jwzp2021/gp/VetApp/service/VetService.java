package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class VetService {
  private final VetRepository vetRepository;

  @Autowired
  public VetService(VetRepository vetRepository) {
    this.vetRepository = vetRepository;
  }

  public Vet getVetById(int id) {
    log.debug("Looking up vet with id=" +id);
    var vet = vetRepository.findById(id);
    return vet.orElseThrow(
        () -> {
          throw new VetNotFoundException("Vet with id:" + id + " not found.");
        });
  }

  public List<Vet> getAll() {
    log.debug("Looking up all vets");
    return vetRepository.findAll();
  }

  public Vet createVet(VetRequestDto vetRequestDto) {
    log.debug("Creating vet for: " + vetRequestDto);
    return vetRepository.save(VetMapper.toVet(vetRequestDto));

//    log.debug("Creating vet for: " + vetRequestDto);
//    Vet v;
//    var vet = VetMapper.toVet(vetRequestDto);
//    try{
//      v = vetRepository.save(vet);
//    }catch (Exception e){
//      log.error("Error while saving vet to database");
//      throw e;
//    }
//    log.info("Vet created successfully");
//    return v;
  }

  public Vet deleteVet(int id) {
    log.debug("Deleting vet with id=" + id);
    var vet = getVetById(id);
    vetRepository.delete(vet);
    return vet;
  }

  public boolean vetAvailable(LocalDateTime startTime, Duration duration, int vetId) {
    log.debug("Checking availability of vet with id=" + vetId + " between " + startTime + " and " + startTime.plusMinutes(duration.toMinutes()));
    var vet = getVetById(vetId);
    return startTime.plus(duration).toLocalTime().isBefore(vet.getShiftEnd().plusSeconds(1))
        && startTime.toLocalTime().isAfter(vet.getShiftStart().minusSeconds(1));
  }
}
