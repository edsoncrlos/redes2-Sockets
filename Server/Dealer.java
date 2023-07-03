
import java.util.Stack;
import java.util.Collections;

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

class PlayerThreadRead implements Runnable {
    private InputStream client;
    private Player player;

    PlayerThreadRead (InputStream client, Player player) {
        this.client = client;
        this.player = player;
    }

    public void run() {
        Scanner s = new Scanner(this.client);
        
        while (s.hasNextLine()) {
            try {
                int clientMessage = Integer.parseInt(s.nextLine().trim());
                if (clientMessage == 1) {
                    // this.player.printMessages();
                }
                if (clientMessage == 2) {
                    // this.player.printMessages();
                }
                // this.server.printMessagesClient(s.nextLine()); 
            } catch (NumberFormatException e) {
                this.player.printMessages("Somente números.");
            }
            
        }
        s.close();
    }
}