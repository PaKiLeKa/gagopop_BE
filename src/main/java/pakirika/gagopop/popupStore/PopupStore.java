package pakirika.gagopop.popupStore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopupStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime StartDate;
    private LocalDateTime EndDate;
    private boolean isOpened;
    private double latitude;
    private double longitude;

//    private String StartDate;
//    private String EndDate;

    public PopupStore(Long id, String name) {
        this.id=id;
        this.name=name;
    }
}
