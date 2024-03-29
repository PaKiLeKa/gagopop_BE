package pakirika.gagopop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pakirika.gagopop.entity.PopupStore;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopupWishDTO {

    private boolean isInWishlist;

    private PopupStore popupStore;


}
