package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Status;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitRepresentation extends RepresentationModel<VisitRepresentation> {
  private final int id;
  private final LocalDateTime startTime;
  private final Duration duration;
  private final Status status;
  private final BigDecimal price;

  public static VisitRepresentation fromVisit(Visit v) {
    return new VisitRepresentation(
        v.getId(), v.getStartTime(), v.getDuration(), v.getStatus(), v.getPrice());
  }
}
