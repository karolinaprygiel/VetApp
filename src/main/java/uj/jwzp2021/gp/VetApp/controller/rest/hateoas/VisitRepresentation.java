package uj.jwzp2021.gp.VetApp.controller.rest.hateoas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;
import uj.jwzp2021.gp.VetApp.model.entity.VisitStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class VisitRepresentation extends RepresentationModel<VisitRepresentation> {
  private final int id;
  private final int animalId;
  private final int clientId;
  private final int vetId;
  private final LocalDateTime startTime;
  private final Duration duration;
  private final VisitStatus status;
  private final BigDecimal price;
  private final String description;

  public static VisitRepresentation fromVisit(Visit v) {
    log.debug("Creating new VisitRepresentation");
    return new VisitRepresentation(
        v.getId(),
        v.getAnimal().getId(),
        v.getClient().getId(),
        v.getVet().getId(),
        v.getStartTime(),
        v.getDuration(),
        v.getVisitStatus(),
        v.getPrice(),
        v.getDescription());
  }
}
