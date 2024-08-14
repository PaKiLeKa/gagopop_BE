package pakirika.gagopop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.PopupStoreRepository;
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
    private final PopupStoreRepository popupStoreRepository;

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

    @Transactional
    public boolean addPopupStore(Long togoListId, UserEntity userEntity, PopupStore popupStore){

        Optional<TogoList> optionalTogoList = togoListRepository.findByIdAndUserEntity( togoListId, userEntity );

        if(optionalTogoList.isEmpty()){
            return  false;
        }

        TogoList togoList=optionalTogoList.get();

        boolean added = togoList.addPopupStore(popupStore);

        if (added) {
            togoListRepository.save(togoList);
        }

        return added;
    }

    @Transactional
    public boolean removePopupStore(Long togoListId, UserEntity userEntity, PopupStore popupStore){

        Optional<TogoList> optionalTogoList = togoListRepository.findByIdAndUserEntity( togoListId, userEntity );

        if(optionalTogoList.isEmpty()){
            return  false;
        }

        TogoList togoList=optionalTogoList.get();

        boolean deleted = togoList.removePopupStore(popupStore);

        if (deleted) {
            togoListRepository.save(togoList);
        }

        return deleted;
    }


//    @Transactional
//    public TogoList addPopupStoreToTogoList(Long togoListId, Long popupStoreId) {
//        Optional<TogoList> togoListOptional = togoListRepository.findById(togoListId);
//        Optional<PopupStore> popupStoreOptional = popupStoreRepository.findById(popupStoreId);
//
//        if (togoListOptional.isPresent() && popupStoreOptional.isPresent()) {
//            TogoList togoList = togoListOptional.get();
//            PopupStore popupStore = popupStoreOptional.get();
//
//            if (togoList.addPopupStore(popupStore)) {
//                return togoListRepository.save(togoList);
//            } else {
//                throw new IllegalStateException("Cannot add more than 5 popup stores to a togo list");
//            }
//        } else {
//            throw new IllegalStateException("TogoList or PopupStore not found");
//        }
//    }




    public List<TogoList> getAllTogoLists(UserEntity userEntity){
        List<TogoList> togoLists = togoListRepository.findByUserEntity( userEntity );
        return togoLists;
    }

    public Optional<TogoList> getTogoList(UserEntity userEntity, Long togoId){
        Optional<TogoList> optionalTogoList=togoListRepository.findByIdAndUserEntity( togoId, userEntity );

        return optionalTogoList;
    }

    @Transactional
    public TogoList createTogoList(UserEntity userEntity, String togoName){

        TogoList togoList = new TogoList();
        togoList.setUserEntity( userEntity );
        togoList.setName( togoName );

        TogoList save=togoListRepository.save( togoList );

        return save;
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