import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {

    private final List<Card> hand;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(this.hand);
    }

    public int getHandValue() {
        return this.hand.stream().mapToInt(card -> card.getType().getValue()).sum();
    }

    public int getSoftHandValue() {
        return this.hand.stream().mapToInt(card -> card.getType() == CardType.ACE ? 1 : card.getType().getValue()).sum();
    }

    public int getBetterHandValue() {

        int hardValue = getHandValue();
        int softValue = getHandValue();

        return hardValue > 21 ? getSoftHandValue() > 21 ? hardValue : softValue : hardValue;
    }

    public void addCard(final Card card) {
        this.hand.add(card);
    }

    public boolean isValidHand() {
        return this.getBetterHandValue() != -1;
    }

}
