package cat.itacademy.s05.t01.model.dto;

import cat.itacademy.s05.t01.model.enums.Move;
import lombok.Getter;

@Getter
public class ActionDTO {

    private Move move;
    private int bet;
}
