import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    static int PORT = 1200;
    private String[] cards = {"A", "1", "2", "3", "4", "5", "6", "7", "8", "9", "J", "Q", "K"};
    private Stack<String> deck;

    public static void main(String[] args) {
        Server server = new Server(PORT);
        // new Dealer();
    }

    Dealer () {
        // this.deck = new ArrayList<String>();
        this.deck = new Stack<String>();
        this.makeDeck();
        this.hit();
    }

    private void makeDeck () {
        // this.deck.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.cards.length; j++) {
                this.deck.push(cards[j]);
            }
        }

        Collections.shuffle(this.deck);        
    }

    private String hit() {
        return this.deck.pop();
    }

    private void stand() {

    }
}
