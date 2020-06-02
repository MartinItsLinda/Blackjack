import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;

public class Blackjack {

    public static void main(String[] args) {

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            new Blackjack().start(reader);
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

        System.out.println(String.format("Player starts off with %s of %s and %s of %s (total hand value: %d), hit or stand?",
                playerInitialCards[0].getType().getFriendlyName(), playerInitialCards[0].getSuit().getFriendlyName(),
                playerInitialCards[1].getType().getFriendlyName(), playerInitialCards[1].getSuit().getFriendlyName(),
                playerHand.getHandValue()));

        System.out.println(String.format("Dealer starts off with %s of %s and %s of %s (total hand value: %d)",
                dealerInitialCards[0].getType().getFriendlyName(), dealerInitialCards[0].getSuit().getFriendlyName(),
                dealerInitialCards[1].getType().getFriendlyName(), dealerInitialCards[1].getSuit().getFriendlyName(),
                dealerHand.getHandValue()));

        try {

            String input = null;

            boolean isStanding = false;
            while (isStanding || (input = reader.readLine()) != null && !input.equalsIgnoreCase("exit")) {

                if (!isStanding) {
                    if (input.equalsIgnoreCase("hit")) {

                        final Card card = deck.drawCard();
                        playerHand.addCard(card);

                        if (playerHand.getHandValue() > 21) {
                            System.out.println(String.format("You drew %s of %s and bust (%d)", card.getType().getFriendlyName(), card.getSuit().getFriendlyName(), playerHand.getHandValue()));
                            isStanding = true;
                        } else {
                            System.out.println(String.format("You drew %s of %s, you're currently at %d. Would you like to hit or stand?", card.getType().getFriendlyName(), card.getSuit().getFriendlyName(), playerHand.getHandValue()));
                        }

                    } else if (input.equalsIgnoreCase("stand")) {
                        System.out.println("You're now standing, dealer will continue to draw until either it hits 17 or busts");
                        isStanding = true;
                    } else {
                        System.out.println(String.format("Invalid option: %s", input));
                    }
                } else {

                    if (dealerHand.getHandValue() >= 17) {
                        if (dealerHand.getHandValue() == 21) {
                            System.out.println(String.format("Dealer wins: %d", dealerHand.getHandValue()));
                        } else {
                            if (dealerHand.getHandValue() > 21 || dealerHand.getHandValue() < playerHand.getHandValue() && playerHand.isValidHand()) {
                                System.out.println("Player wins");
                            } else if (playerHand.getHandValue() > 21 || playerHand.getHandValue() < dealerHand.getHandValue() && dealerHand.isValidHand()) {
                                System.out.println("Dealer wins");
                            } else {
                                System.out.println("Everyone bust!");
                            }
                        }
                        break;
                    } else {

                        final Card card = deck.drawCard();
                        dealerHand.addCard(card);

                        System.out.println(String.format("Dealer drew %s of %s (%d)", card.getType().getFriendlyName(), card.getSuit().getFriendlyName(), dealerHand.getHandValue()));

                    }

                }

            }

        } catch (final IOException ignored) {
        }

    }



}
