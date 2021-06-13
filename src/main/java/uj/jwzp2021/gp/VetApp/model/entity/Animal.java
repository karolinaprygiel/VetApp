package uj.jwzp2021.gp.VetApp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "animals")
public class Animal {

  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Visit> visits = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private AnimalType type;
  private String name;
  private int yearOfBirth;
  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
  private Client owner;

}
