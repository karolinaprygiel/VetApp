package uj.jwzp2021.gp.VetApp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.VisitStatus;
@Data
@AllArgsConstructor
public class VisitUpdateRequestDto {
  @JsonProperty("status")
  private VisitStatus visitStatus;
  private String description;
}
