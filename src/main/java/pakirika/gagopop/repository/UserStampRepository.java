package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.UserStamp;

import java.util.List;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
    List<Stamp> findByUserEntity(UserEntity userEntity);
}
