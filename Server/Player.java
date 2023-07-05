import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;

public class Player implements Runnable {
    private PrintStream writePlayer;
    private InputStream outputPlayer;
    private int id;
    private List<String> cards;
    private boolean stand = false;
    private Dealer dealer;

    Player (PrintStream writePlayer, InputStream outputPlayer, int id) {
        this.writePlayer = writePlayer;
        this.outputPlayer = outputPlayer;
        this.id = id;
        this.cards = new ArrayList<String>();
    }

    public void hit(String card) {
        this.cards.add(card);
    }

    public void showCurrentCards() {
        this.printMessages(this.currentCards());
    }
    
    private String currentCards() {
        String message = "You:";
        for (int index = 0; index < this.cards.size(); index++) {
            message += " " + this.cards.get(index);
        }
        return message;
    }

    public void stand() {
        this.stand = true;
    }

    public boolean getStand() {
        return stand;
    }

    public InputStream getOutputPlayer() {
        return this.outputPlayer;
    }

    public void printMessages(String message) {
        this.writePlayer.println(message);
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public int getId() {
        return this.id;
    }

    public List<String> getCards() {
        return this.cards;
    }

    public void run() {
        Scanner s = new Scanner(this.outputPlayer);
        
        while (s.hasNextLine()) {
            try {
                int clientMessage = Integer.parseInt(s.nextLine().trim());
                if (clientMessage == 1) {
                    this.hit(this.dealer.hit());
                    this.showCurrentCards();
                }
                if (clientMessage == 2) {
                    this.dealer.stand();
                }

            } catch (NumberFormatException e) {
                this.printMessages("Somente números.");
            } catch (EmptyStackException e) {
                this.printMessages("As cartas acabaram!!");
            } 
        }
        s.close();
    }
}
