package pakirika.gagopop.dto;


import lombok.Getter;
import lombok.Setter;
import pakirika.gagopop.entity.PopupStore;
import pakirika.gagopop.entity.Wishlist;

@Getter
@Setter
public class WishTogoDTO {

    private boolean isInTogo;

    private PopupStore popupStore;


}
