package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.UserStamp;

import java.util.List;
import java.util.Optional;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
    List<Stamp> findByUserEntity(UserEntity userEntity);

    Optional<UserStamp> findByStampId(Long StampId );
    Long countByUserEntity(UserEntity userEntity);

}
