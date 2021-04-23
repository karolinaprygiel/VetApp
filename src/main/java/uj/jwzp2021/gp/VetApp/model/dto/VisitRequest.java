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
public class VisitRequest {
    private final LocalDateTime startTime;
    private final Duration duration;
    private final Animal animal;
    private final BigDecimal price;
    private final Client client;
    private final Vet vet;
    private final String description;
    private final Status status;
}
