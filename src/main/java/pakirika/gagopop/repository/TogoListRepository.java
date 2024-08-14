package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.TogoList;
import pakirika.gagopop.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface TogoListRepository extends JpaRepository<TogoList, Long> {

    List<TogoList> findByUserEntity(UserEntity userEntity);

    Long countByUserEntity(UserEntity userEntity );

    //List<TogoList> findByUserEntityAndPopupStore(UserEntity userEntity, PopupStore popupStore);

    Optional<TogoList> findByIdAndUserEntity(Long id, UserEntity userEntity);
}
