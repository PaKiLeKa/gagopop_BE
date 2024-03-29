package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.TogoListRepository;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TogoListService {

    private final TogoListRepository togoListRepository;

    public List<PopupStore> getAllPopupStoresInUserTogoList(UserEntity userEntity) {
        //UserEntity으로 검색해서 보내주기
        List<TogoList> togoLists = togoListRepository.findByUserEntity(userEntity);
        List<PopupStore> popupStores = new ArrayList<>(); //팝업스토어 객체를 넣을 리스트

        if(togoLists.isEmpty()){
            return Collections.emptyList();
        }
//        for (TogoList togoList : togoLists) {
//            List<WishlistPopupStore> wishlistPopupStores = togoList.get;
//            for (WishlistPopupStore wishlistPopupStore : wishlistPopupStores) {
//                popupStores.add(wishlistPopupStore.getPopupStore());
//            }
//        }
        return popupStores;
    }



}