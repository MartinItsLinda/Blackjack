import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.StringJoiner;

public class Blackjack {

    public static void main(String[] args) {

        final Blackjack blackjack = new Blackjack();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean start;
            do {
                start = blackjack.start(reader);
            } while (start);
        } catch (final IOException ex) {
            System.err.println("Couldn't close reader: " + ex.getMessage());
        }

    }

    public boolean start(final BufferedReader reader) {

        final Deck deck = new Deck(new ArrayList<>(EnumSet.allOf(CardType.class)),
                                   new ArrayList<>(EnumSet.allOf(CardSuit.class)),
                                   200);

        final Hand dealerHand = new Hand();
        final Hand playerHand = new Hand();

        final Card[] playerInitialCards = deck.drawCards(2);
        final Card[] dealerInitialCards = deck.drawCards(2);

        playerHand.addCard(playerInitialCards[0]);
        playerHand.addCard(playerInitialCards[1]);

        dealerHand.addCard(dealerInitialCards[0]);
        dealerHand.addCard(dealerInitialCards[1]);

        System.out.println(generateStartingHandDrawnMessage(playerInitialCards, playerHand, false));
        System.out.println(generateStartingHandDrawnMessage(dealerInitialCards, dealerHand, true));

        System.out.println("\nWould you like to hit or stand?");

        try {

            String input = null;

            boolean isStanding = false;
            boolean finished = false;

            while(true) {

                if (finished || !isStanding) input = reader.readLine();

                if (finished) { //we have to do this at the top because only then will we get the users yes or no response
                    return input.toLowerCase().equals("y") || input.equalsIgnoreCase("yes");
                }

                if (!isStanding) {
                    if (input.equalsIgnoreCase("hit")) {

                        final Card card = deck.drawCard();
                        playerHand.addCard(card);

                        System.out.println(generateHandDrawnMessage(card, playerHand, false));

                        if (!playerHand.isValidHand()) {
                            isStanding = true;
                        } else {
                            System.out.println("Hit or stand?");
                        }

                    } else if (input.equalsIgnoreCase("stand")) {
                        if (dealerHand.getBetterHandValue() == playerHand.getBetterHandValue()) {
                            System.out.println("Push, nobody wins");
                        } else {
                            System.out.println("You're now standing, dealer will continue to draw until either it hits 17 or busts");
                            isStanding = true;
                        }
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
                            } else if (dealerHand.getBetterHandValue() > playerHand.getBetterHandValue()) {
                                System.out.println("Dealer wins");
                            } else if (dealerHand.getBetterHandValue() == playerHand.getBetterHandValue()) {
                                System.out.println("You both had the same number, push. Nobody wins!");
                            } else {
                                System.out.println("Player wins");
                            }
                        }

                        System.out.println(String.format("Player Deck: %s", generateDeckMessage(playerHand)));
                        System.out.println(String.format("Dealer Deck: %s", generateDeckMessage(dealerHand)));

                        System.out.println("Go again? Type yes or no");

                        finished = true;

                    } else {

                        final Card card = deck.drawCard();
                        dealerHand.addCard(card);

                        System.out.println(generateHandDrawnMessage(card, dealerHand, true));

                    }

                }

            }

        } catch (final IOException ignored) {
        }

        return false;
    }

    public String generateDeckMessage(final Hand hand) {
        final StringJoiner joiner = new StringJoiner(", ");
        for (final Card card : hand.getHand()) {
            joiner.add(String.format("%s of %s", card.getType().getFriendlyName(), card.getSuit().getFriendlyName()));
        }
        joiner.add(Integer.toString(hand.getBetterHandValue()));
        return joiner.toString();
    }

    //i know this is very repetitive and most definitely not the best way of doing it
    public String generateStartingHandDrawnMessage(final Card[] cards, final Hand hand, boolean dealer) {
        if (hand.getHandValue() > 21) {
            if (dealer) {
                return String.format("Dealer drew %s of %s giving him a hand of %d",
                        cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                        cards[0].getType().getValue());
            } else {
                return String.format("You drew %s of %s and %s of %s giving you a hand of %d",
                        cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                        cards[1].getType().getFriendlyName(), cards[1].getSuit().getFriendlyName(),
                        hand.getSoftHandValue());
            }
        } else {
            if (hand.getHandValue() != hand.getSoftHandValue()) {
                if (dealer) {
                    return String.format("Dealer drew %s of %s giving him %d",
                            cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                            cards[0].getType().getValue());
                } else {
                    return String.format("You drew %s of %s and %s of %s giving you %d (Soft %d)",
                            cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                            cards[1].getType().getFriendlyName(), cards[1].getSuit().getFriendlyName(),
                            hand.getHandValue(),
                            hand.getSoftHandValue());
                }
            } else {
                if (dealer) {
                    return String.format("Dealer drew %s of %s giving him %d",
                            cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                            cards[0].getType().getValue());
                } else {
                    return String.format("You drew %s of %s and %s of %s giving you %d",
                            cards[0].getType().getFriendlyName(), cards[0].getSuit().getFriendlyName(),
                            cards[1].getType().getFriendlyName(), cards[1].getSuit().getFriendlyName(),
                            hand.getHandValue());
                }
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
