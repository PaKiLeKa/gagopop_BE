package pakirika.gagopop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.repository.PopupStoreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PopupStoreService {

    private final PopupStoreRepository popupStoreRepository;

    public Optional<PopupStore> getPopupStore(Long id){

        Optional<PopupStore> result =popupStoreRepository.findById( id );

        return result;

    }





    //루트 찾기 전 sort
    public List<PopupStore> sortPopupStore (double latitude, double longitude, List<Long> pid){
        List<PopupStore> popupList = new ArrayList<>();
        List<PopupStore> resultList = new ArrayList<>();

        // pid로 popup 객체를 조회하여 popupList에 저장
        for (long id : pid) {
            Optional<PopupStore> popup = popupStoreRepository.findById(id); // findById 메소드는 Repository에 맞게 사용
            if (popup.isPresent()) {
                popupList.add(popup.get());
            }
        }

        // popupList가 비어있을 때까지 반복해야하므로
        while (!popupList.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            PopupStore closestPopup = null;

            // 출발지 좌표와 가장 가까운 팝업 객체를 찾음
            for (PopupStore popup : popupList) {
                double distance = calculateDistance(latitude, longitude, popup.getLatitude(), popup.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPopup = popup;
                }
            }

            // 가장 가까운 팝업을 결과 리스트에 추가하고 popupList에서 제거
            if (closestPopup != null) {
                resultList.add(closestPopup);
                popupList.remove(closestPopup);
            }
        }

        return resultList;
    }

    // 두 지점 간의 거리를 계산하는 메소드 (예: Haversine 공식 사용)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }


    public List<PopupStore> getPopupStoreScheduledToOpen(LocalDate date){

        List<PopupStore> scheduledToOpenStoreByDate=popupStoreRepository.findStoreScheduledToOpenByDate( date );


        return scheduledToOpenStoreByDate;
    }

    public List<PopupStore> getPopupStoreScheduledToClose(LocalDate date){

        List<PopupStore> scheduledToOpenStoreByDate=popupStoreRepository.findStoreScheduledToCloseByDate( date );


        return scheduledToOpenStoreByDate;
    }


}
