package uj.jwzp2021.gp.VetApp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "vets")
public class Vet {

  @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Visit> visits = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  private String surname;
  private LocalTime shiftStart;
  private LocalTime shiftEnd;
}
