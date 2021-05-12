package uj.jwzp2021.gp.VetApp.model.dto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import uj.jwzp2021.gp.VetApp.model.entity.VisitStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitResponseDto {
    private int id;
    private LocalDateTime startTime;
    private Duration duration;
    private VisitStatus status;
    private BigDecimal price;
    private int animalId;
    private int clientId;
    private int vetId;
    private int officeId;
    private String description;

}
