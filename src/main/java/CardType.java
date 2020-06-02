public enum CardType {

    ACE("Ace", 1),
    TWO("Two", 2),
    THREE("Three", 3),
    FOUR("Four", 4),
    FIVE("Five", 5),
    SIX("Six", 6),
    SEVEN("Seven", 7),
    EIGHT("Eight", 8),
    NINE("Nine", 9),
    JACK("Jack", 10),
    QUEEN("Queen", 10),
    KING("King", 10);

    private String friendlyName;
    private int value;

    CardType(final String friendlyName,
             final int value) {
        this.friendlyName = friendlyName;
        this.value = value;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public int getValue() {
        return this.value;
    }
}
