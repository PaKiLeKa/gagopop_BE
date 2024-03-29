package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.TogoList;
import pakirika.gagopop.entity.UserEntity;

import java.util.List;

public interface TogoListRepository extends JpaRepository<TogoList, Long> {

    List<TogoList> findByUserEntity(UserEntity userEntity);

}
