package pakirika.gagopop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.WishlistPopupStore;

import java.util.List;

public interface WishlistPopupStoreRepository extends JpaRepository<WishlistPopupStore, Long> {

    List<WishlistPopupStore> findByWishlist_UserEntity(UserEntity userEntity);

}
