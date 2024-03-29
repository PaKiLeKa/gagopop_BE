package pakirika.gagopop.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pakirika.gagopop.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {



        UserEntity findByUsername(String username);

}
