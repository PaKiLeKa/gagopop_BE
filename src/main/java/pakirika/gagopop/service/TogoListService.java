package pakirika.gagopop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.dto.TogoListDTO;
import pakirika.gagopop.entity.*;
import pakirika.gagopop.repository.PopupStoreRepository;
import pakirika.gagopop.repository.TogoListRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    public TogoListDTO toDto(TogoList togoList) {
        Set<Long> popupStoreIds = togoList.getPopupStores()
                .stream()
                .filter(Objects::nonNull)
                .map(PopupStore::getId)
                .collect(Collectors.toSet());

        return new TogoListDTO(togoList, popupStoreIds);
    }
}