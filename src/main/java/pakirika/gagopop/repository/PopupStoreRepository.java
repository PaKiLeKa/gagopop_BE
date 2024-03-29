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

    @Query(value="SELECT * FROM popup_store  WHERE start_date>= :date ORDER BY start_date ASC LIMIT 6",
            nativeQuery=true)
    List<PopupStore> findStoreScheduledToOpenByDate(@Param("date") LocalDate date);

    @Query(value="SELECT * FROM popup_store  WHERE end_date >= :date ORDER BY end_date ASC LIMIT 6",
            nativeQuery=true)
    List<PopupStore> findStoreScheduledToCloseByDate(@Param("date") LocalDate date);
    @Query(value="SELECT * FROM popup_store  WHERE start_date<= :date AND" +
            " end_date >= :date and is_opened is not null LIMIT 6",
            nativeQuery=true)
    List<PopupStore> findSixStores(@Param("date") LocalDate date);

    List<PopupStore> findAll();

}
