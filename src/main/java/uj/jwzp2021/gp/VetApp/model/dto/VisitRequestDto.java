package uj.jwzp2021.gp.VetApp.model.dto;

import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.model.entity.Status;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class VisitRequestDto {
    private final LocalDateTime startTime;
    private final Duration duration;
    private final BigDecimal price;
    private final int animalId;
    private final int clientId;
    private final int vetId;
}
