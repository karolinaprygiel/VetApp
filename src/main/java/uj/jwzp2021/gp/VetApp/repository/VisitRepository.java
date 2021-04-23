package uj.jwzp2021.gp.VetApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uj.jwzp2021.gp.VetApp.model.entity.Visit;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query("select v from visits v where (:timeFrom >= v.startTime and :timeFrom < (v.startTime + v.duration)) or (:timeTo > v.startTime and :timeTo <= (v.startTime + v.duration))")
    List<Visit> overlaps(LocalDateTime timeFrom, LocalDateTime timeTo);

    @Transactional
    @Modifying
    @Query("update visits v set v.status = uj.jwzp2021.gp.VetApp.model.entity.Status.FINISHED_AUTOMATICALLY WHERE (v.startTime + v.duration) < :time and v.status = uj.jwzp2021.gp.VetApp.model.entity.Status.PLANNED")
    void finishOutOfDateVisits(LocalDateTime time);
}
