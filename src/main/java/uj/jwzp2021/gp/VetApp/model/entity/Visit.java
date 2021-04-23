package uj.jwzp2021.gp.VetApp.model.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="visits")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private LocalDateTime startTime;

    @Column(columnDefinition = "interval")
    private Duration duration;

    private Status status;

    private BigDecimal price;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "vet_id")
    private Vet vet;

    private String description;

    public static Visit newVisit(LocalDateTime startTime, Duration duration, Animal animal, BigDecimal price, Client client, Vet vet) {
        return new Visit(-1, startTime, duration, Status.PLANNED, price, animal, client, vet, null);
    }


}
