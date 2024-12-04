package cat.itacademy.s05.t01.model.card;

import cat.itacademy.s05.t01.model.enums.Suit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Card {


    @Schema(description = "Value of the card", example = "11")
    private int value;
    @Schema(description = "Suit of the card", examples = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES", })
    private Suit suit;

    public Card(Suit suit) {
        this.suit = suit;
    }
}
