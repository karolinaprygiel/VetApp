package uj.jwzp2021.gp.VetApp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.core.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

}
