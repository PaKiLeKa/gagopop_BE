package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.WishTogoDTO;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.TogoListPopupStoreRepository;
import pakirika.gagopop.repository.UserRepository;
import pakirika.gagopop.repository.WishlistPopupStoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final UserRepository userRepository;
    private final WishlistPopupStoreRepository wishlistPopupStoreRepository;
    private final TogoListPopupStoreRepository togoListPopupStoreRepository;


    public List<WishTogoDTO> getPopupStoresInWishlistWithTogoByUserId(Long userId) {
        // 해당 userId로 유저 조회
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // 유저가 존재하지 않을 경우 null 반환
            return null;
        }

        // 해당 유저의 WishlistPopupStore 조회
        List<WishlistPopupStore> wishlistPopupStores = wishlistPopupStoreRepository.findByWishlist_UserEntity(user);

        // WishlistPopupStore에 연결된 PopupStore 객체들을 가져옴
        return wishlistPopupStores.stream()
                .map(wishlistPopupStore -> {
                    PopupStore popupStore = wishlistPopupStore.getPopupStore();
                    boolean isInTogoList = isPopupStoreInTogoList(popupStore);
                    WishTogoDTO wishTogoDTO = new WishTogoDTO();
                    wishTogoDTO.setInTogo(isInTogoList);
                    wishTogoDTO.setPopupStore(popupStore);
                    return wishTogoDTO;
                })
                .collect(Collectors.toList());
    }

    private boolean isPopupStoreInTogoList(PopupStore popupStore) {
        List<TogoListPopupStore> togoListPopupStores = togoListPopupStoreRepository.findByPopupStore(popupStore);
        return !togoListPopupStores.isEmpty();
    }

}
