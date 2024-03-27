package pakirika.gagopop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.Wishlist;
import pakirika.gagopop.entity.WishlistPopupStore;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<PopupStore> getAllPopupStoresInUserWishlist(String username) {
        List<Wishlist> wishlists = wishlistRepository.findByUserEntity_Username(username);
        List<PopupStore> popupStores = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            List<WishlistPopupStore> wishlistPopupStores = wishlist.getWishlistPopupStores();
            for (WishlistPopupStore wishlistPopupStore : wishlistPopupStores) {
                popupStores.add(wishlistPopupStore.getPopupStore());
            }
        }
        return popupStores;
    }
}
