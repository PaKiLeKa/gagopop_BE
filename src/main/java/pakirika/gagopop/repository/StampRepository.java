package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.Stamp;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByUser_Name(String userName);
}
