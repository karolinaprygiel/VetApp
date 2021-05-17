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
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

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

  public boolean worksAtMidnight(Vet vet) {
    var startTime = vet.getShiftStart();
    var endTime = vet.getShiftStart().plus(vet.getWorkingTime());
    return startTime.isAfter(endTime);
  }

  public boolean isVetAtWork(LocalDateTime startTime, Duration duration, int vetId) {
    var vet = getVetById(vetId);

    // vet 15.04 20:00 - 16.04 4:00
    // wizyty:
    // 15.04 21:00 - 15 MIN
    // 15.04 23:55 - 15 MIN koniec 16.04
    // 16.04 02:22 - 15 MIN
    // Vet - start datetime i duration
    // Wizyta - start time i duration

    var worksAtMidnight = worksAtMidnight(vet);

    if (!worksAtMidnight) {
      /* simplest case - vet works regular shift*/
      var vetWorkStart = vet.getShiftStart().atDate(startTime.toLocalDate());
      var vetWorkEnd =
          vet.getShiftStart().plus(vet.getWorkingTime()).atDate(startTime.toLocalDate());
      return startTime.isAfter(vetWorkStart.minusSeconds(1))
          && startTime.plus(duration).isBefore(vetWorkEnd.plusSeconds(1));
    } else {
      /* two cases to consider when vet is at work at midnight */
      var visitCrossesMidnight =
          startTime.toLocalTime().isAfter(startTime.toLocalTime().plus(duration));
      if (visitCrossesMidnight) {
        /* vet and visit "synchronize" - relatively simple case */
        var vetWorkStart = vet.getShiftStart().atDate(startTime.toLocalDate());
        var vetWorkEnd =
            vet.getShiftStart()
                .plus(vet.getWorkingTime())
                .atDate(startTime.toLocalDate().plusDays(1));
        return startTime.isAfter(vetWorkStart.minusSeconds(1))
            && startTime.plus(duration).isBefore(vetWorkEnd.plusSeconds(1));
      } else {
        /* vet works at midnight, but visit does not cross it - let's split the vet's shift into two shifts */
        var vetWorkStart1= LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIDNIGHT);;
        var vetWorkEnd1 = LocalDateTime.of(startTime.toLocalDate().minusDays(1), vet.getShiftStart()).plus(vet.getWorkingTime());

        var vetWorkStart2 = LocalDateTime.of(startTime.toLocalDate(), vet.getShiftStart());
        var vetWorkEnd2 = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(23,59,59, 59));
        return (startTime.isAfter(vetWorkStart1.minusSeconds(1))
                && startTime.plus(duration).isBefore(vetWorkEnd1.plusSeconds(1)))
            || (startTime.isAfter(vetWorkStart2.minusSeconds(1))
                && startTime.plus(duration).isBefore(vetWorkEnd2.plusSeconds(1)));
      }
    }
  }
}
