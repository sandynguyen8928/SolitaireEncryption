import java.util.Random;

public class Deck {
    public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
    public static Random gen = new Random();

    public int numOfCards; // contains the total number of cards in the deck
    public Card head; // contains a pointer to the card on the top of the deck

    public Deck(int numOfCardsPerSuit, int numOfSuits) {

        // Throwing an exception if numOfCardsPerSuits or numOfSuits is out of bounds
        if ( numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13 || numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
            throw new IllegalArgumentException();
        }

        // Assigning the number of cards of the deck to numOfCards
        numOfCards = (numOfCardsPerSuit * numOfSuits + 2);

        // Assigning the head to be the first card
        this.head = new PlayingCard(suitsInOrder[0], 1 );

        // Creating a temporary variable temp that stores the head
        Card temp = this.head;

        // Iterating through each suit
        for (int i = 0; i < numOfSuits; i++) {

            // Iterating numOfCardsPerSuit times
            for (int j = 1; j <= numOfCardsPerSuit; j++) {

                // Already created the first card
                if (i==0 && j==1) {
                    continue;
                }
                // Any other cards that isn't the first card
                else {
                    // Assigning the temp's next card to be the new card (after creating it)
                    temp.next = new PlayingCard(suitsInOrder[i], j );

                    // Creating a temporary previous card tempPrev to be future previous card
                    Card tempPrev = temp;

                    // Updating the temp card to be the new card we just created
                    temp = temp.next;

                    // The old temp becomes the previous card for the new card
                    temp.prev = tempPrev;
                }
            }
        }

        // Creating the Jokers
        Joker jokerRed = new Joker("red");
        Joker jokerBlack = new Joker("black");

        // Connecting the Jokers with each other + the head/tail
        temp.next = jokerRed;
        jokerRed.next = jokerBlack;
        jokerBlack.next = this.head;

        jokerRed.prev = temp;
        jokerBlack.prev = jokerRed;
        this.head.prev = jokerBlack;
    }

    public Deck(Deck d) {

        // Assigning this deck's number of cards to be the input deck's number of cards
        this.numOfCards = d.numOfCards;

        // Assigning this deck's head to be the input deck's head
        this.head = d.head.getCopy();

        // Creating temporary variables which are the two deck's head
        Card tempThis = this.head;
        Card tempInput = d.head;

        // Looping numOfCards-1 (since we already assigned the head) times
        for (int i = 0; i < d.numOfCards-1; i++) {

            // Creating a temporary variable tempThisPrev which is going to be the future previous card
            Card tempThisPrev = tempThis;

            // Updating the tempInput reference to be its next card
            tempInput = tempInput.next;

            // Assigning this deck's next card to be the input deck's next card
            tempThis.next = tempInput.getCopy();

            // Updating this deck's card to be its next card
            tempThis = tempThis.next;

            // Assigning this deck's previous card to be tempThisPrev
            tempThis.prev = tempThisPrev;
        }

        // Connecting this deck's head and tail together
        tempThis.next = this.head;
        this.head.prev = tempThis;
    }

    public Deck() {}

    public void addCard(Card c) {

        // If this deck is empty, adding c and connecting next and prev to it
        if (this.head == null) {
            this.head = c;
            this.head.prev = c;
            this.head.next = c;
        }

        // If this deck is not empty
        else {
            // Connecting the new last card (c) with this deck's old last card and its head
            Card prevTemp = this.head.prev;
            this.head.prev = c;
            c.prev = prevTemp;
            prevTemp.next = c;
            c.next = this.head;
        }
        this.numOfCards++;
    }

    public void shuffle() {

        // Creating an array
        Card[] cardsArray = new Card[this.numOfCards];

        // Looping to get this deck's cards into the array
        for (int i = 0; i < this.numOfCards; i++) {

            // Assigning the array's ith position the head
            cardsArray[i] = this.head;

            // Updating the head to be its next one
            this.head = this.head.next;
        }

        // Looping to shuffle the cards in the array
        for (int i = numOfCards - 1; i >= 1; i--) {

            // Creating a variable that will store a random number
            int j = gen.nextInt(i + 1);

            // Swapping the card from the array at i position with the card at j position
            Card temp;
            temp = cardsArray[j];
            cardsArray[j] = cardsArray[i];
            cardsArray[i] = temp;
        }

        // If the array is empty, the head is null
        if (cardsArray.length == 0) {
            this.head = null;
        }

        // If the array is not empty
        else {
            // Assigning this deck's head to be the first element of the array
            this.head = cardsArray[0];
            Card temp = cardsArray[0];

            // Looping through each element of the array
            for (int i = 1; i < cardsArray.length; i++) {

                // Getting each card from the array back into this deck
                Card tempPrev = temp;
                temp.next = cardsArray[i];
                temp = temp.next;
                temp.prev = tempPrev;
            }

            // Connecting this deck's head and tail together
            temp.next = this.head;
            this.head.prev = temp;
        }
    }

    public Joker locateJoker(String color) {

        // Creating two variables: a Joker theJoker and the other a temporary head temp
        Joker theJoker = null;
        Card temp = this.head;

        // Looping through the length of the deck
        for (int i=0; i < numOfCards; i++) {

            // Checking if the card is a type Joker. If it is, checking if its color matches the input color
            if (temp instanceof Joker && ((Joker) temp).getColor().equals(color)) {

                // Updating theJoker's reference to be temp
                theJoker = (Joker) temp;
            }

            // If the card is not a type Joker
            else {
                // Updating temp to be its next card
                temp = temp.next;
            }
        }
        return theJoker;
    }

    public void moveCard(Card c, int p) {

        // Checking if p is not 0 (not moving)
        if (p != 0) {

            // Creating temporary variables storing the card, card's next and card's previous references
            Card temp = c;
            Card prevTemp = c.prev;
            Card nextTemp = c.next;

            // Removing the card c (now temp) from the deck and connecting its previous and next card together
            prevTemp.next = nextTemp;
            nextTemp.prev = prevTemp;

            // Assigning c to be the one after p position
            for (int i = 0; i < p; i++) {
                c = c.next;
            }
            // c is now the card before the position you wanna add the card, temp is THE card you wanna add back

            // Updating nextTemp to be c's next card
            nextTemp = c.next;

            // Adding and connecting temp back into the deck
            nextTemp.prev = temp;
            c.next = temp;
            temp.prev = c;
            temp.next = nextTemp;
        }
    }

    public void tripleCut(Card firstCard, Card secondCard) {

        // Dividing the deck in two decks
        // This deck's head until firstCard's previous card is deck1
        Card firstHead = this.head;
        Card firstEnd = firstCard.prev;

        // secondCard's next card and this deck's tail is deck2
        Card secondHead = secondCard.next;
        Card secondEnd = this.head.prev;

        // If firstCard is the head
        if (firstCard == this.head && secondCard != this.head.prev) {

            // Put the deck2 in front of this deck's head
            secondEnd.next = firstCard;
            firstCard.prev = secondEnd;

            // Reassigning this deck's head and tail
            this.head = secondHead;
            this.head.prev = secondCard;
        }

        // If secondCard is the tail
        else if (firstCard != this.head && secondCard == this.head.prev) {

            // Put deck1 after this deck's tail
            firstHead.prev = secondCard;
            secondCard.next = firstHead;

            // Reassigning this deck's head and tail
            this.head = firstCard;
            this.head.prev = firstEnd;
        }

        // If firstCard or secondCard aren't head or tail
        else {
            // Connecting this deck's tail with firstCard
            secondEnd.next = firstCard;
            firstCard.prev = secondEnd;

            // Connecting this deck's head with secondCard
            secondCard.next = firstHead;
            firstHead.prev = secondCard;

            // Connecting the new deck's head and tail together
            secondHead.prev = firstEnd;
            firstEnd.next = secondHead;

            // Reassigning this deck's head and tail
            this.head = secondHead;
            this.head.prev = firstEnd;
        }
    }

    public void countCut() {

        // Creating variables: number is the number of cards we have to remove from the head, endCard is this deck's tail
        int number = this.head.prev.getValue() % this.numOfCards;
        Card endCard = this.head.prev;

        // Checking if number is not 0 (no change if it is)
        if (number != 0) {

            // Looping number times
            for (int i = 0; i < number; i++) {

                // Moving the last card one position to the right
                moveCard(endCard, 1);
            }

            // Reassigning this deck's head and tail
            this.head = endCard.next;
            this.head.prev = endCard;
        }
    }

    public Card lookUpCard() {

        // Creating variables: a temporary head and a counter
        Card temp = this.head;
        int counter = 0;

        // Looping as long as counter doesn't reach the value of the deck's head
        while(counter != this.head.getValue()) {

            // Updating temp to be its next card + Incrementing counter by 1
            temp = temp.next;
            counter++;
        }
        // Checking if temp is of type Playing card, if so return temp
        if (temp instanceof PlayingCard) {
            return temp;
        }
        // If temp is of type Joker, return null
        return null;
    }

    public int generateNextKeystreamValue() {

        // Creating a variable which will store the future key value
        int keyValue = 0;

        // Looping as long as keyvalue has the value of 0
        while (keyValue == 0) {

            // Locating the two Jokers and storing them into variables
            Joker redJoker = locateJoker("red");
            Joker blackJoker = locateJoker("black");

            // Moving the Jokers in its right position
            moveCard(redJoker, 1);
            moveCard(blackJoker, 2);

            // FOR TRIPLE CUT
            // Creating variables: a temporary head temp, a jokerColor and two Jokers
            Card temp = this.head;
            String jokerColor = null;
            Joker joker1;
            Joker joker2;

            // Looping through the length of the deck
            for (int i = 0; i < numOfCards; i++) {

                // Checking if temp is of type Joker. If so, assign jokerColor to be temp's color
                if (temp instanceof Joker) {
                    jokerColor = ((Joker) temp).getColor();
                    break;
                }
                // If temp is not of type Joker, updating temp to be its next card
                else {
                    temp = temp.next;
                }
            }

            // Assigning joker1 and joker2 its respective Jokers
            if (jokerColor == "red") {
                joker1 = redJoker;
                joker2 = blackJoker;
            } else {
                joker1 = blackJoker;
                joker2 = redJoker;
            }

            // Perform triple cut
            this.tripleCut(joker1, joker2);
            // END OF TRIPLE CUT

            // Perform count cut
            this.countCut();

            // Update keyValue if the keystream card isn't a Joker
            if (this.lookUpCard() != null) {
                keyValue = this.lookUpCard().getValue();
            }
        }
        return keyValue;
    }

    public abstract class Card {
        public Card next;
        public Card prev;

        public abstract Card getCopy();
        public abstract int getValue();
    }

    public class PlayingCard extends Card {
        public String suit;
        public int rank;

        public PlayingCard(String s, int r) {
            this.suit = s.toLowerCase();
            this.rank = r;
        }

        public String toString() {
            String info = "";
            if (this.rank == 1) {
                //info += "Ace";
                info += "A";
            } else if (this.rank > 10) {
                String[] cards = {"Jack", "Queen", "King"};
                //info += cards[this.rank - 11];
                info += cards[this.rank - 11].charAt(0);
            } else {
                info += this.rank;
            }
            //info += " of " + this.suit;
            info = (info + this.suit.charAt(0)).toUpperCase();
            return info;
        }

        public PlayingCard getCopy() {
            return new PlayingCard(this.suit, this.rank);
        }

        public int getValue() {
            int i;
            for (i = 0; i < suitsInOrder.length; i++) {
                if (this.suit.equals(suitsInOrder[i]))
                    break;
            }

            return this.rank + 13*i;
        }

    }

    public class Joker extends Card{
        public String redOrBlack;

        public Joker(String c) {
            if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
                throw new IllegalArgumentException("Jokers can only be red or black");

            this.redOrBlack = c.toLowerCase();
        }

        public String toString() {
            //return this.redOrBlack + " Joker";
            return (this.redOrBlack.charAt(0) + "J").toUpperCase();
        }

        public Joker getCopy() {
            return new Joker(this.redOrBlack);
        }

        public int getValue() {
            return numOfCards - 1;
        }

        public String getColor() {
            return this.redOrBlack;
        }
    }

}
 