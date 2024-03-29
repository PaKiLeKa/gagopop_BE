package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.WishlistPopupStore;
import pakirika.gagopop.repository.WishlistPopupStoreRepository;
import pakirika.gagopop.repository.WishlistRepository;

@Service
@RequiredArgsConstructor
public class WishlistPopupStoreService {
    private final WishlistRepository wishlistRepository;
    private final WishlistPopupStoreRepository wishlistPopupStoreRepository;

    public void addToWishlist(UserEntity user, PopupStore popupStore) {
        WishlistPopupStore wishlistPopupStore = new WishlistPopupStore();
        wishlistPopupStore.setWishlist(wishlistRepository.findByUserEntity( user ));
        wishlistPopupStore.setPopupStore(popupStore);
        wishlistPopupStoreRepository.save(wishlistPopupStore);
    }

/*    public PopupStore getPopupStoreById(Long popupStoreId) {
        // popupStoreId에 해당하는 PopupStore 조회하여 반환
    }*/
}
