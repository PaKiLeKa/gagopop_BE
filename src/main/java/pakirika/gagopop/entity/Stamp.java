package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "popup_store_id", referencedColumnName = "id")
    private PopupStore popupStore;

    private String imageUrl;

}
