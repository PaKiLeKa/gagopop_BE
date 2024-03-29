package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.TogoListPopupStore;

import java.util.List;

public interface TogoListPopupStoreRepository extends JpaRepository<TogoListPopupStore, Long> {
    List<TogoListPopupStore> findByPopupStore(PopupStore popupStore);
}
