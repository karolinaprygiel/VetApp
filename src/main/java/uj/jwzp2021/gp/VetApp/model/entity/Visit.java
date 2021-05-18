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
@Entity(name = "visits")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
public class Visit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(columnDefinition = "interval")
  private Duration duration;

  @Column(name = "visit_status")
  private VisitStatus visitStatus;

  private BigDecimal price;

  @ManyToOne
  @JoinColumn(name = "animal_id", referencedColumnName = "id")
  private Animal animal;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @ManyToOne
  @JoinColumn(name = "vet_id", referencedColumnName = "id")
  private Vet vet;

  @ManyToOne
  @JoinColumn(name = "office_id", referencedColumnName = "id")
  private Office office;

  private String description;


}
