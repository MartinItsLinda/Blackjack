public class Card {

    private CardSuit suit;
    private CardType type;

    Card(final CardSuit suit,
         final CardType type) {
        this.suit = suit;
        this.type = type;
    }

    public CardSuit getSuit() {
        return this.suit;
    }

    public CardType getType() {
        return this.type;
    }

}
