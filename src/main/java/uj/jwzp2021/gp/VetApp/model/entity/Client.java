package uj.jwzp2021.gp.VetApp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIgnoreProperties({"hibernateLazyInitializer","referenceList", "animals"})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String surname;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private final List<Animal> animals = new ArrayList<>();
//
//    @OneToMany(mappedBy = "clients")
//    private final List<Animal> animals;


    public static Client newClient(String name, String surname){
        return new Client(-1, name, surname);
    }
}
