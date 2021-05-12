package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class VetRequestDto {
  private final String name;
  private final String surname;
  private final String shiftStart;
  private final String shiftEnd;
}
