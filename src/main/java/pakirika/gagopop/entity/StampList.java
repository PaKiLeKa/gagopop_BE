package pakirika.gagopop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class StampList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Stamp stamp;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private UserEntity userEntity;
}
