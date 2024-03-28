package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.Stamp;
import pakirika.gagopop.entity.UserEntity;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByUserEntity(UserEntity userEntity);
}
