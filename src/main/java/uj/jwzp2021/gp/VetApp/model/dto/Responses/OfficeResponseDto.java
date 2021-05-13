package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OfficeResponseDto {
  private final int id;
  private final String name;
  private final List<Integer> visitIds;
}
