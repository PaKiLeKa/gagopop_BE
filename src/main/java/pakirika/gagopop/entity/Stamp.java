package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "popup_store_id", referencedColumnName = "id")
    private PopupStore popupStore;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity user;
}
