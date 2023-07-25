import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;

public class Player implements Runnable {
    private PrintStream playerSend;
    private InputStream PlaterReceive;
    private int id;
    private List<String> cards;
    private boolean stand = false;
    private Dealer dealer;
    private Socket playerSocket;

    Player (Socket playerSocket, int id) {
        this.playerSocket = playerSocket;
        try {
            this.playerSend = new PrintStream(playerSocket.getOutputStream());
            this.PlaterReceive = playerSocket.getInputStream();;
        } catch (Exception e) {
            System.out.println(e);
        }
        this.id = id;
        this.cards = new ArrayList<String>();
    }

    public void newGame() {
        if (!this.cards.isEmpty()) {
            this.cards.clear();
        }
        this.stand = false;
    }

    public void clearScreen() {
        this.playerSend.print("\033[H\033[2J");
        this.playerSend.flush();
    }

    public void hit(String card) {
        this.cards.add(card);
    }

    public void showCurrentCards() {
        this.printMessages(this.currentCards("You:"));
    }
    
    public String currentCards(String name) {
        String message = name;
        for (int index = 0; index < this.cards.size(); index++) {
            message += " " + this.cards.get(index);
        }
        message += ": " + this.dealer.getScore(this.cards);

        return message;
    }

    public void stand() {
        this.stand = true;
    }

    public boolean getStand() {
        return stand;
    }

    public InputStream PlaterReceive() {
        return this.PlaterReceive;
    }

    public void printMessages(String message) {
        this.playerSend.println(message);
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
        Scanner s = new Scanner(this.PlaterReceive);

        while (s.hasNextLine()) {
            try {
                int clientMessage = Integer.parseInt(s.nextLine().trim());

                if (clientMessage == 0) {
                    this.dealer.newGame();
                }

                if (!this.dealer.isAvailable()) {
                    if (clientMessage == 1) {
                        this.hit(this.dealer.hit());
                        this.showCurrentCards();
                    }
                    if (clientMessage == 2) {
                        this.stand();
                        this.dealer.stand(this);
                    }
                    if (clientMessage == 3) {
                        this.playerSocket.close();
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                this.printMessages("Somente números.");
            } catch (EmptyStackException e) {
                this.printMessages("Sem cartas!!");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        s.close();
        this.dealer.removePlayer(this);
    }
}
