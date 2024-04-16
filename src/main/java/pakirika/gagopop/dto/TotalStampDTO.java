package pakirika.gagopop.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TotalStampDTO {

    Long totalStamp;
    Long monthlyStamp;
    List<SimpleStampDto> stampList;

}
