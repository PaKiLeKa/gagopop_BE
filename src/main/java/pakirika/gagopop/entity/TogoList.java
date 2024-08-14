package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class TogoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private UserEntity userEntity;

    @ManyToMany
    @JoinTable(
            name = "togo_popup_store",
            joinColumns = @JoinColumn(name = "togo_list_id"),
            inverseJoinColumns = @JoinColumn(name = "popup_store_id")
    )
    private Set<PopupStore> popupStores = new HashSet<>();

    // Method to add a PopupStore ensuring no duplicates and max 5 stores
    public boolean addPopupStore(PopupStore popupStore) {
        if (popupStores.size() < 5) {
            return popupStores.add(popupStore);
        }
        return false;
    }

    // Method to remove a PopupStore
    public boolean removePopupStore(PopupStore popupStore) {
        return popupStores.remove(popupStore);
    }



//    @ManyToOne
//    @JoinColumn(name="popup_id", referencedColumnName="id")
//    private PopupStore popupStore;
//
//    @ManyToOne
//    @JoinColumn(name="popup_id", referencedColumnName="id")
//    private PopupStore popupSecond;
//
//    @ManyToOne
//    @JoinColumn(name="popup_id", referencedColumnName="id")
//    private PopupStore popupThird;
//
//    @ManyToOne
//    @JoinColumn(name="popup_id", referencedColumnName="id")
//    private PopupStore popupFourth;
//
//    @ManyToOne
//    @JoinColumn(name="popup_id", referencedColumnName="id")
//    private PopupStore popupFifth;




    /*- 이름
- 유저ID (FK)
- 생성일
- 팝업 1 (FK)
- 팝업 2 (FK)
- 팝업 3 (FK)
- 팝업 4 (FK)
- 팝업 5 (FK)
- 경로 가능 여부? - 굳이 ? → 그때그때 연산 ? → 트리거 넣어서 팝업 넣고 뺄 때마다 업데이트?*/

}
