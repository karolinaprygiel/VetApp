package uj.jwzp2021.gp.VetApp.model.dto.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uj.jwzp2021.gp.VetApp.model.entity.VisitStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitUpdateRequestDto {
  @JsonProperty("status")
  private VisitStatus visitStatus;
  private String description;
}
