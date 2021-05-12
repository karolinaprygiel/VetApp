package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientResponseDto {
  private final int id;
  private final String name;
  private final String surname;
}