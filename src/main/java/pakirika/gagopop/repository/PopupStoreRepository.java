package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pakirika.gagopop.entity.PopupStore;

import java.time.LocalDate;
import java.util.List;

public interface PopupStoreRepository extends JpaRepository<PopupStore,Long> {



    List<PopupStore> findByNameContaining(String name);

    @Query(value="SELECT * FROM popup_store  WHERE name like %:name% AND " +
            "start_date<= :date AND end_date >= :date and is_opened is not null",
            nativeQuery=true)
    List<PopupStore> findOpenPopupStoresByNameDate(@Param("name") String name, @Param("date") LocalDate date);

    List<PopupStore> findAll();

}
