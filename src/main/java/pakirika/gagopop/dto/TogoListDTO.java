package pakirika.gagopop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pakirika.gagopop.entity.TogoList;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TogoListDTO {

    private Long id;
    private String name;
    private Set<Long> popupStoreIds;

    public TogoListDTO(TogoList togoList, Set<Long> popupStoreIds) {
        this.id = togoList.getId();
        this.name = togoList.getName();
        this.popupStoreIds = popupStoreIds;
    }

    //todo
    //+ 팝업스토어 개수
    //+ 경로탐색 가능 여부
}
