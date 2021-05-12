package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfficeRequestDto {
  private final String name;

  public OfficeRequestDto() {
    this.name = null;
  }
}


