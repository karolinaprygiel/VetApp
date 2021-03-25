package uj.jwzp2021.gp.VetApp.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "animal")
public class Animal {
    @Id
    @GeneratedValue
    private Long id;

    AnimalType type;
    String name;
    int yearOfBirth;
}
