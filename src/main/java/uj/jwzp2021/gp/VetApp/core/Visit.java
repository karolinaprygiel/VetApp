package uj.jwzp2021.gp.VetApp.core;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity(name="visits")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private final int id;

    private final LocalDateTime startTime;
    @Column(columnDefinition = "interval")
    private final Duration duration;
    private final Animal animal;
    private final Status status;
    private final BigDecimal price;

    protected Visit() {
        id = 0;
        startTime = null;
        duration = Duration.ZERO;
        animal = Animal.OTHER;
        status = Status.PLANNED;
        price = null;
    }

    public static Visit newVisit(LocalDateTime startTime, Duration duration, Animal animal, BigDecimal price) {
        return new Visit(-1, startTime, duration, animal, Status.PLANNED, price);
    }
}
