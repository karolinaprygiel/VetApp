package uj.jwzp2021.gp.VetApp.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="vets")
public class Vet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;

    private String surname;

    private LocalDateTime shiftStart;

    private LocalDateTime shiftEnd;

}
