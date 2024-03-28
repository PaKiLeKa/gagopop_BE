package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.UserEntity;
import pakirika.gagopop.entity.Wishlist;
import pakirika.gagopop.entity.WishlistPopupStore;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public List<PopupStore> getAllPopupStoresInUserWishlist(UserEntity userEntity) {
        //UserEntity으로 검색해서 보내주기
        List<Wishlist> wishlists = wishlistRepository.findByUserEntity(userEntity);
        List<PopupStore> popupStores = new ArrayList<>(); //팝업스토어 객체를 넣을 리스트

        if(wishlists.isEmpty()){
            return Collections.emptyList(); //컨트롤러 단에서 받아서 확인해서 빈 리스트라면 예외처리?
        }

        for (Wishlist wishlist : wishlists) {
            List<WishlistPopupStore> wishlistPopupStores = wishlist.getWishlistPopupStores();
            for (WishlistPopupStore wishlistPopupStore : wishlistPopupStores) {
                popupStores.add(wishlistPopupStore.getPopupStore());
            }
        }
        return popupStores;
    }

}
