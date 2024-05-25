package pakirika.gagopop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime StartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime EndDate;
    private String operatingTime;
    private boolean isOpened;
    private double latitude;
    private double longitude;
    private String address;
    private String info;
    private String snsLink;
    private String imageUrl;
    private boolean isPromoted;
    //hotStore 나타내는 필드


}
