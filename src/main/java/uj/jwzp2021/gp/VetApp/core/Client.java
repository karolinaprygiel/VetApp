package uj.jwzp2021.gp.VetApp.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Entity(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private final int id;

    private final String name;
    private final String surnname;


    public Client() {
        id = 0;
        name = null;
        surnname = null;
    }
}
