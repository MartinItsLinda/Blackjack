import java.security.SecureRandom;
import java.util.*;

public class Deck {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final List<CardType> types;
    private final List<CardSuit> suits;
    private final int size;

    private List<Card> deck;

    public Deck(final List<CardType> types,
                final List<CardSuit> suits,
                final int size) {
        this.types = types;
        this.suits = suits;
        this.size = size;

        this.deck = generateDeck(types, suits, size);
    }

    public List<Card> getDeck() {
        return Collections.unmodifiableList(this.deck);
    }

    public Card drawCard() {
        if (this.deck.isEmpty()) {
            this.deck = this.generateDeck(this.types, this.suits, this.size);
        }
        return this.deck.remove(0);
    }

    public Card[] drawCards(final int cards) {
        if (cards < 1) throw new IllegalArgumentException("Cannot draw less than 1 card");

        if (cards == 1) {
            return new Card[]{drawCard()};
        }

        final Card[] cardsToDraw = new Card[cards];
        for (int i = 0; i < cards; i++) {
            cardsToDraw[i] = drawCard();
        }

        return cardsToDraw;
    }

    public void refreshDeck() {
        this.deck = this.generateDeck(this.types, this.suits, this.size);
    }

    private List<Card> generateDeck(final List<CardType> types,
                                    final List<CardSuit> suits,
                                    final int size) {
        if (types.isEmpty()) throw new IllegalArgumentException("Cannot generate deck with no card types");
        if (suits.isEmpty()) throw new IllegalArgumentException("Cannot generate deck with no card suits");
        if (size < 1) throw new IllegalArgumentException("Cannot generate deck size less than 1");

        final List<Card> deck = new ArrayList<>(size);

        for (int i = 0; i < size; i++ ) {
            deck.add(new Card(suits.get(RANDOM.nextInt(suits.size())),
                              types.get(RANDOM.nextInt(types.size()))));
        }

        Collections.shuffle(deck);

        return deck;
    }

}
