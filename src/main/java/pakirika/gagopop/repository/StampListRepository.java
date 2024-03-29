package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.StampList;
import pakirika.gagopop.entity.UserEntity;

public interface StampListRepository extends JpaRepository<StampList, Long> {
    Long countByUserEntity(UserEntity userEntity);
}
