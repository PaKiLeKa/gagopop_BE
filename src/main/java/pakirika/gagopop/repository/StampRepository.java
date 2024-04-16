package pakirika.gagopop.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByUserEntity(UserEntity userEntity);

    @Query("SELECT s FROM Stamp s WHERE MONTH(s.date) = MONTH(:date) AND s.userEntity = :user")
    List<Stamp> findByUserAndMonth(@Param("user") UserEntity user, @Param("date") LocalDate date);


    @Query("SELECT COUNT(s) FROM Stamp s WHERE MONTH(s.date) = MONTH(:date) AND s.userEntity = :user")
    long countByUserAndMonth(@Param("user") UserEntity user, @Param("date") LocalDate date);

    Optional<Stamp> findByUserEntityAndPopupStore(UserEntity userEntity, PopupStore popupStore);

/*
    Optional<UserStamp> findByStampId(Long StampId );
*/
    Long countByUserEntity(UserEntity userEntity);


}
