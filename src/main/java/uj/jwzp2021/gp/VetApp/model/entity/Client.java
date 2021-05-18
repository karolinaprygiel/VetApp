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
@Entity(name = "clients")
public class Client {

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Animal> animals = new ArrayList<>();
  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Visit> visits = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String name;
  private String surname;


}
