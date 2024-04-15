package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.Stamp;

import java.util.Optional;


public interface StampRepository extends JpaRepository<Stamp, Long> {

    Optional<Stamp> findByPopupStore(PopupStore popupStore);
}
