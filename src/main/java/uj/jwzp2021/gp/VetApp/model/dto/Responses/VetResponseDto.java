package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class VetResponseDto {
  private final int id;
  private final String name;
  private final String surname;
  private final String shiftStart;
  private final String shiftEnd;
  private final List<Integer> visitIds;
}
