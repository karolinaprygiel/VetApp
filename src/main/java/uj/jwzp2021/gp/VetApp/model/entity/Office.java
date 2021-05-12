package uj.jwzp2021.gp.VetApp.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "offices")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String name;

}
