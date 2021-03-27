package uj.jwzp2021.gp.VetApp.controllers.rest;

import lombok.Data;
import uj.jwzp2021.gp.VetApp.core.Animal;
import uj.jwzp2021.gp.VetApp.core.Client;
import uj.jwzp2021.gp.VetApp.core.Vet;

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

}
