package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "stamp_id", referencedColumnName = "id")
    private Stamp stamp;


    private LocalDateTime date;

    private String picture;

    private String content;

    private String withWho;
}
