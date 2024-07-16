package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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


    @ManyToOne
    @JoinColumn(name="popup_id", referencedColumnName="id")
    private PopupStore popupStore;
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
