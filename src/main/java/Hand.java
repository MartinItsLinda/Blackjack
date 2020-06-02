import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {

    private long userId;
    private List<Card> hand;

    public Hand(final long userId) {
        this.userId = userId;
        this.hand = new ArrayList<>();
    }

    public long getUserId() {
        return this.userId;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(this.hand);
    }

    public int getHandValue() {
        return this.hand.stream().mapToInt(card -> card.getType().getValue()).sum();
    }

    public void addCard(final Card card) {
        this.hand.add(card);
    }

    public boolean isValidHand() {
        return this.getHandValue() <= 21;
    }

}
