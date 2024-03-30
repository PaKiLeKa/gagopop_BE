package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.Wishlist;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    //List<Wishlist> findByUserEntity_Username(String username);
    List<Wishlist> findByUserEntity(UserEntity userEntity);

    List<Wishlist> findByPopupStore(PopupStore popupStore);

    Optional<Wishlist> findByUserEntityAndPopupStore(UserEntity userEntity, PopupStore popupStore);

    Long countByUserEntity(UserEntity userEntity);
}
