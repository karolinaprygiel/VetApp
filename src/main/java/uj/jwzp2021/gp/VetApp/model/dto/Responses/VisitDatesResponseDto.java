package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitDatesResponseDto {
  private LocalDateTime startTime;
  private Duration duration;
  private int vetId;
  private int officeId;
}
