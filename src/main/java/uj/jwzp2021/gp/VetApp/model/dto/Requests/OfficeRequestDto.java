package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class OfficeRequestDto {
  private final String name;

  public OfficeRequestDto() {
    this.name = null;
  }
}
