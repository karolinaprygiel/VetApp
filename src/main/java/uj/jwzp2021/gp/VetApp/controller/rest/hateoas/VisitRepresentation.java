package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.VisitStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitRepresentation extends RepresentationModel<VisitRepresentation> {
  private final int id;
  private final LocalDateTime startTime;
  private final Duration duration;
  private final VisitStatus status;
  private final BigDecimal price;

  public static VisitRepresentation fromVisit(VisitResponseDto v) {
    return new VisitRepresentation(
        v.getId(),
        v.getStartTime(),
        v.getDuration(),
        v.getStatus(),
        v.getPrice());
  }
}
