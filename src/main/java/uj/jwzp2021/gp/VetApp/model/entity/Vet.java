package uj.jwzp2021.gp.VetApp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="vets")
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String surname;


    private LocalDateTime shiftStart;

    private LocalDateTime shiftEnd;

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Visit> visits = new ArrayList<>();



}
