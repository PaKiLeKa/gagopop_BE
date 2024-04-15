package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByUserEntity(UserEntity userEntity);

    Optional<Stamp> findByUserEntityAndPopupStore(UserEntity userEntity, PopupStore popupStore);

/*
    Optional<UserStamp> findByStampId(Long StampId );
*/
    Long countByUserEntity(UserEntity userEntity);

}
