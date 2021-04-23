package uj.jwzp2021.gp.VetApp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;
    private String surname;

//    @OneToMany(mappedBy = "clients")
//    private final List<Visit> visits;
//
//    @OneToMany(mappedBy = "clients")
//    private final List<Animal> animals;

}
