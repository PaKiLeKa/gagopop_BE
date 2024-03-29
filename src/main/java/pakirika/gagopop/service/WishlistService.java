package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.PopupWishDTO;
import pakirika.gagopop.dto.WishTogoDTO;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.TogoListPopupStoreRepository;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.repository.WishlistPopupStoreRepository;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final UserRepository userRepository;

    private final WishlistRepository wishlistRepository;
    private final TogoListPopupStoreRepository togoListPopupStoreRepository;


    public void addToWishlist(UserEntity user, PopupStore popupStore) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUserEntity( user);
        wishlist.setPopupStore(popupStore);
        wishlistRepository.save(wishlist);
    }



    public List<WishTogoDTO> getPopupStoresInWishlistWithTogoByUserId(Long userId) {
        // 해당 userId로 유저 조회
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // 유저가 존재하지 않을 경우 null 반환
            return null;
        }

        // 해당 유저의 WishlistPopupStore 조회
        List<Wishlist> wishlists=wishlistRepository.findByUserEntity( user );

        // WishlistPopupStore에 연결된 PopupSt
        // ore 객체들을 가져옴
        return wishlists.stream()
                .map(wishlist -> {
                    PopupStore popupStore = wishlist.getPopupStore();
                    boolean isInTogoList = isPopupStoreInTogoList(popupStore);
                    WishTogoDTO wishTogoDTO = new WishTogoDTO();
                    wishTogoDTO.setInTogo(isInTogoList);
                    wishTogoDTO.setPopupStore(popupStore);
                    return wishTogoDTO;
                })
                .collect(Collectors.toList());
    }


    public List<PopupWishDTO> getPopupStoresWithWishlist(Long userId) {
        // 해당 userId로 유저 조회
        UserEntity user = userRepository.findById(userId).orElse(null);
        // 해당 유저의 WishlistPopupStore 조회
        List<Wishlist> wishlists=wishlistRepository.findByUserEntity( user );

        // WishlistPopupStore에 연결된 PopupSt
        // ore 객체들을 가져옴
        return wishlists.stream()
                .map(wishlist -> {
                    PopupStore popupStore = wishlist.getPopupStore();
                    boolean isInWishlist = isPopupStoreInWishList(popupStore);


                    PopupWishDTO popupWishDTO = new PopupWishDTO();
                    popupWishDTO.setInWishlist(isInWishlist);
                    popupWishDTO.setPopupStore(popupStore);
                    return popupWishDTO;
                })
                .collect(Collectors.toList());
    }

    private boolean isPopupStoreInWishList(PopupStore popupStore) {
        List<Wishlist> wishlistPopupStores = wishlistRepository.findByPopupStore(popupStore);
        return !wishlistPopupStores.isEmpty();
    }

    private boolean isPopupStoreInTogoList(PopupStore popupStore) {
        List<TogoListPopupStore> togoListPopupStores = togoListPopupStoreRepository.findByPopupStore(popupStore);
        return !togoListPopupStores.isEmpty();
    }

}
