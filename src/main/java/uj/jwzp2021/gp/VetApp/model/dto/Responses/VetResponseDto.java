package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Data
public class VetResponseDto {
  private int id;
  private String name;
  private String surname;
  private String shiftStart;
  private String shiftEnd;
}
