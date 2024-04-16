package pakirika.gagopop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class StampDTO {
    private Long stampId;
    private LocalDate date;
    private Long popupId;
    private String popupName; // 팝업 스토어 이름
    private String picture;
    private String content;
    private String withWho;


}
