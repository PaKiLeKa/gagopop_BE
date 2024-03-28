package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class TogoListPopupStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Togo_list_id", referencedColumnName = "id")
    private TogoList togoList;

    @ManyToOne
    @JoinColumn(name = "popup_store_id", referencedColumnName = "id")
    private PopupStore popupStore;

}
