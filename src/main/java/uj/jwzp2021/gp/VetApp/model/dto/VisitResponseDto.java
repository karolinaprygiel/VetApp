package uj.jwzp2021.gp.VetApp.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class VisitResponseDto {
    private int id;
    private LocalDateTime startTime;
    private Duration duration;
    private String status;
    private BigDecimal price;
    private int animalId;
    private int clientId;
    private int vetId;
    private String description;

}
