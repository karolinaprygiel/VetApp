package uj.jwzp2021.gp.VetApp.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private AnimalType type;

    private String name;

    private int yearOfBirth;
    //private int monthOfBirth;
    //private int dayOfBirth;
    //private LocalDateTime birthDate;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client owner;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Visit> visits = new ArrayList<>();





    public static Animal newAnimal(AnimalType type, String name, int yearOfBirth, Client owner){
        return new Animal(-1, type, name, yearOfBirth, owner);
    }

}
