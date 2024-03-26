package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pakirika.gagopop.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
        //Optional<UserEntity> findByEmail(String email);

        UserEntity findByUsername(String username);


}
