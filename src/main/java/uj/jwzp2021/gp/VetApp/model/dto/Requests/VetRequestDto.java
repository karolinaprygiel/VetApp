package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class VetRequestDto {
  private final String name;
  private final String surname;
  private final String shiftStart;
  private final Duration workingTime;
}
