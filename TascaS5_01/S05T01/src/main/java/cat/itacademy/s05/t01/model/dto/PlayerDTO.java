package cat.itacademy.s05.t01.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDTO {

    private Long id;
    private String username;
    private int gamesWon; //???

    public PlayerDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
