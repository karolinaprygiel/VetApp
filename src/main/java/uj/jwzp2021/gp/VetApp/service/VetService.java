package uj.jwzp2021.gp.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    log.info("Looking up vet with id=" +id);
    var vet = vetRepository.findById(id);
    return vet.orElseThrow(
        () -> {
          throw new VetNotFoundException("Vet with id:" + id + " not found.");
        });
  }

  public List<Vet> getAll() {
    log.info("Looking up all vets");
    return vetRepository.findAll();
  }

  public Vet createVet(VetRequestDto vetRequestDto) {
    log.info("Creating vet for: " + vetRequestDto);
    Vet vet;
    try{
      vet = vetRepository.save(VetMapper.toVet(vetRequestDto));
    }catch (DataAccessException ex){
      log.error("Repository problem while saving vet for request: " + vetRequestDto);
      throw ex;
    }
    log.info("Vet for request: " + vetRequestDto + " created successfully");
    return vet;

  }

  public Vet deleteVet(int id) {
    log.info("Deleting vet with id=" + id);
    var vet = getVetById(id);
    try{
      vetRepository.delete(vet);
    }catch (DataAccessException ex){
      log.error("Repository error while deleting vet with id=" + id);
      throw ex;
    }
    log.info("Vet with id=" + id + " deleted successfully");
    return vet;
  }

  public boolean vetAvailable(LocalDateTime startTime, Duration duration, int vetId) {
    log.debug("Checking availability of vet with id=" + vetId + " between " + startTime + " and " + startTime.plusMinutes(duration.toMinutes()));
    var vet = getVetById(vetId);
    return startTime.plus(duration).toLocalTime().isBefore(vet.getShiftEnd().plusSeconds(1))
        && startTime.toLocalTime().isAfter(vet.getShiftStart().minusSeconds(1));
  }
}
