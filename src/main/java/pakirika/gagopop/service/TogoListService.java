package pakirika.gagopop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.TogoListRepository;
import pakirika.gagopop.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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


    public Optional<TogoList> showTogoList(UserEntity userEntity, Long togoId){
        Optional<TogoList> optionalTogoList=togoListRepository.findByIdAndUserEntity( togoId, userEntity );

        return optionalTogoList;
    }

    @Transactional
    public TogoList createTogoList(UserEntity userEntity, String togoName){

        TogoList togoList = new TogoList();
        togoList.setUserEntity( userEntity );
        togoList.setName( togoName );

        togoListRepository.save( togoList );

        return togoList;
    }

    @Transactional
    public boolean deleteTogoList(UserEntity userEntity, TogoList togoList){

        Optional<TogoList> optionalTogoList =togoListRepository.findByIdAndUserEntity(togoList.getId() ,userEntity);

        if(optionalTogoList.isPresent()){
            togoListRepository.delete(optionalTogoList.get());
            return true;
        }

        return false;
    }

}