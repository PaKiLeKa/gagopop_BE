package pakirika.gagopop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pakirika.gagopop.entity.UserEntity;


@Getter
@Setter
@NoArgsConstructor
public class UserProfileDTO {

    private String email;

    private String nickname;

    private Long wishlistTotal;

    private Long togolistTotal;

    private Long stampTotal;
}
