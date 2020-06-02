import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;

public class Blackjack {

    public static void main(String[] args) {

        final Blackjack blackjack = new Blackjack();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            blackjack.start(reader);
        } catch (final IOException ex) {
            System.err.println("Couldn't close reader: " + ex.getMessage());
        }

    }

    public void start(final BufferedReader reader) {

        final Deck deck = new Deck(new ArrayList<>(EnumSet.allOf(CardType.class)),
                                   new ArrayList<>(EnumSet.allOf(CardSuit.class)),
                                   200);

        final Hand dealerHand = new Hand(0);
        final Hand playerHand = new Hand(1);

        final Card[] playerInitialCards = deck.drawCards(2);
        final Card[] dealerInitialCards = deck.drawCards(2);

        playerHand.addCard(playerInitialCards[0]);
        playerHand.addCard(playerInitialCards[1]);

        dealerHand.addCard(dealerInitialCards[0]);
        dealerHand.addCard(dealerInitialCards[1]);

        System.out.println(generateStartingHandDrawnMessage(playerInitialCards, playerHand, false));
        System.out.println(generateStartingHandDrawnMessage(dealerInitialCards, dealerHand, true));

        try {

            String input = null;

            boolean isStanding = false;
            while (isStanding || (input = reader.readLine()) != null && !input.equalsIgnoreCase("exit")) {

                if (!isStanding) {
                    if (input.equalsIgnoreCase("hit")) {

                        final Card card = deck.drawCard();
                        playerHand.addCard(card);

                        System.out.println(generateHandDrawnMessage(card, playerHand, false));

                        if (playerHand.getHandValue() > 21 && playerHand.getSoftHandValue() > 21) {
                            isStanding = true;
                        }

                    } else if (input.equalsIgnoreCase("stand")) {
                        System.out.println("You're now standing, dealer will continue to draw until either it hits 17 or busts");
                        isStanding = true;
                    } else {
                        System.out.println(String.format("Invalid option: %s", input));
                    }

                } else {

                    if (dealerHand.getBetterHandValue() >= 17) {
                        if (dealerHand.getBetterHandValue() == 21) {
                            System.out.println("Dealer Wins!");
                        } else {
                            if (!dealerHand.isValidHand() && !playerHand.isValidHand()) {
                                System.out.println("Everyone bust, nobody wins!");
                            } else if (dealerHand.getBetterHandValue() > playerHand.getBetterHandValue() && dealerHand.isValidHand()) {
                                System.out.println("Dealer wins");
                            } else {
                                System.out.println("Player wins");
                            }
                        }
                        break;
                    } else {

                        final Card card = deck.drawCard();
                        dealerHand.addCard(card);

                        System.out.println(generateHandDrawnMessage(card, dealerHand, true));

                    }

                }

            }

        } catch (final IOException ignored) {
        }

    }

    public String generateStartingHandDrawnMessage(final Card[] cards, final Hand hand, boolean dealer) {
        if (hand.getHandValue() > 21) {
            return String.format("%s drew %s of %s and %s of %s giving %s a hand of %d",
                    (dealer ? "Dealer" : "You"),
                    cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                    cards[1].getType().getFriendlyName(), cards[1].getSuit().getFriendlyName(),
                    (dealer ? "him" : "you"),
                    hand.getSoftHandValue());
        } else {
            if (hand.getHandValue() != hand.getSoftHandValue()) {
                return String.format("%s drew %s of %s and %s of %s giving %s %d (Soft %d)",
                        (dealer ? "Dealer" : "You"),
                        cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                        cards[1].getType().getFriendlyName(), cards[1].getSuit().getFriendlyName(),
                        (dealer ? "him" : "you"),
                        hand.getHandValue(),
                        hand.getSoftHandValue());
            } else {
                return String.format("%s drew %s of %s and %s of %s giving %s %d",
                        (dealer ? "Dealer" : "You"),
                        cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                        cards[1].getType().getFriendlyName(), cards[1].getSuit().getFriendlyName(),
                        (dealer ? "him" : "you"),
                        hand.getHandValue());
            }
        }
    }

    public String generateHandDrawnMessage(final Card card, final Hand hand, boolean dealer) {
        if (hand.getHandValue() > 21) {
            if (hand.getSoftHandValue() > 21) {
                return String.format("%s drew %s of %s and bust (%d)",
                        (dealer ? "Dealer" : "You"),
                        card.getType().getFriendlyName(), card.getSuit().getFriendlyName(),
                        hand.getHandValue());
            } else {
                return String.format("%s drew %s of %s (%d)",
                        (dealer ? "Dealer" : "You"),
                        card.getType().getFriendlyName(), card.getSuit().getFriendlyName(), hand.getSoftHandValue());
            }
        } else {
            if (hand.getHandValue() != hand.getSoftHandValue()) {
                return String.format("%s drew %s of %s (%d, soft %d)",
                        (dealer ? "Dealer" : "You"),
                        card.getType().getFriendlyName(), card.getSuit().getFriendlyName(), hand.getHandValue(), hand.getSoftHandValue());
            } else {
                return String.format("%s drew %s of %s (%d)",
                        (dealer ? "Dealer" : "You"),
                        card.getType().getFriendlyName(), card.getSuit().getFriendlyName(), hand.getHandValue());
            }
        }
    }

}
