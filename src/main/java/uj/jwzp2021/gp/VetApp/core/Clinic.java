package uj.jwzp2021.gp.VetApp.core;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String name;

}