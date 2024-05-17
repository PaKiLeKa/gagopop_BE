package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pakirika.gagopop.entity.PopupStore;

import java.time.LocalDate;
import java.util.List;

public interface PopupStoreRepository extends JpaRepository<PopupStore,Long> {

    List<PopupStore> findByNameContaining(String name);

    List<PopupStore> findAllByOrderByIdDesc();


    @Query(value="SELECT * FROM popup_store  WHERE name like %:keyword% OR address like %:keyword% AND " +
            "start_date<= :date AND end_date >= :date and is_opened is not null",
            nativeQuery=true)
    List<PopupStore> findOpenPopupStoresByNameDate(@Param("keyword") String keyword, @Param("date") LocalDate date);


    //키워드 및 날짜로 검색 + 오픈중
    @Query(value="SELECT * FROM popup_store  WHERE name like %:keyword% OR address like %:keyword% AND " +
            "start_date<= :date AND end_date >= :date and is_opened is not null ORDER BY start_date ASC, end_date ASC",
            nativeQuery=true)
    List<PopupStore> findOpenStoreByKeywordDate(@Param("keyword") String keyword, @Param("date") LocalDate date);


    //키워드 및 날짜로 검색 + 오픈예정
    @Query(value="SELECT * FROM popup_store  WHERE name like %:keyword% OR address like %:keyword% AND " +
            "start_date> :date ORDER BY start_date ASC, end_date ASC",
            nativeQuery=true)
    List<PopupStore> findScheduledStoreByKeywordDate(@Param("keyword") String keyword, @Param("date") LocalDate date);


    //키워드 및 날짜로 검색 + 종료
    @Query(value="SELECT * FROM popup_store  WHERE name like %:keyword% OR address like %:keyword% AND " +
            " end_date < :date ORDER BY end_date ASC",
            nativeQuery=true)
    List<PopupStore> findClosedStoreByKeywordDate(@Param("keyword") String keyword, @Param("date") LocalDate date);


    //키워드 및 날짜로 검색 + 종료임박 7일
    @Query(value="SELECT * FROM popup_store  WHERE name like %:keyword% OR address like %:keyword% AND " +
            " DATEDIFF(end_date, :date) between 0 and 6  ORDER BY end_date ASC",
            nativeQuery=true)
    List<PopupStore> findScheduledToCloseStore(@Param("keyword") String keyword,@Param("date") LocalDate date);



    @Query(value="SELECT * FROM popup_store  WHERE start_date>= :date ORDER BY start_date ASC LIMIT 6",
            nativeQuery=true)
    List<PopupStore> findStoreScheduledToOpenByDate(@Param("date") LocalDate date);

    @Query(value="SELECT * FROM popup_store  WHERE end_date >= :date ORDER BY end_date ASC LIMIT 6",
            nativeQuery=true)
    List<PopupStore> findStoreScheduledToCloseByDate(@Param("date") LocalDate date);
    @Query(value="SELECT * FROM popup_store  WHERE start_date<= :date AND" +
            " end_date >= :date and is_opened is not null LIMIT 5",
            nativeQuery=true)
    List<PopupStore> findFiveStores(@Param("date") LocalDate date);

    @Query(value="SELECT * FROM popup_store  WHERE start_date<= :date AND" +
            " end_date >= :date and is_opened is not null And is_promoted = TRUE ORDER BY end_date",
            nativeQuery=true)
    List<PopupStore> findStoreIsPromoted(@Param("date") LocalDate date);

    List<PopupStore> findAll();

}
