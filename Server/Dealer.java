import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dealer {
    static int PORT = 1200;
    private String[] typesCards = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private Stack<String> deck;
    private Player player;
    private List<String> cards;
    // private boolean stand = false;

    Dealer (Player player) {
        this.deck = new Stack<String>();
        this.cards = new ArrayList<String>();
        this.makeDeck();

        this.player = player;
        this.addPlayer(player);
        this.startDealer();
        this.startPlayer();
    }

    public void addPlayer(Player player) {
        player.setDealer(this);
        new Thread(player).start();;
    }

    private void startDealer() {
        System.out.println(this.deck);
        this.cards.add(this.hit());
        this.cards.add(this.hit());
    }
    
    private void startPlayer() {
        this.player.printMessages("Dealer: " + this.cards.get(0) + " *");
        
        this.player.hit(this.hit());
        this.player.hit(this.hit());
        this.player.showCurrentCards();
        
        this.player.printMessages("1- Hit\n2- Stand\n");
    }


    private void makeDeck () {
        // this.deck.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.typesCards.length; j++) {
                this.deck.push(typesCards[j]);
            }
        }

        Collections.shuffle(this.deck);        
    }

    private String showDealerCards() {
        String message = "Dealer:";
        
        for (int i = 0; i < this.cards.size(); i++) {
            message += " " + this.cards.get(i);
        }

        return message;
    }

    
    public String hit() {
        return this.deck.pop();
    }
    
    public void stand() {
        this.player.stand();
        // TODO: if all players stand
        this.dealerFinish();
        this.winner();
    }

    public int getScore(List<String> cards) {
        int numberAs = 0;
        int score = 0;

        for (int i = 0; i < cards.size(); i++) {
            String card = cards.get(i);
            if (card == "J" || card == "Q" || card == "K") {
                score += 10;
            } else if (card == "A") {
                numberAs += 1;
            } else {
                score += Integer.parseInt(card);
            }
        }
        if (numberAs > 0) {
            if (score + 11 + (numberAs - 1) <= 21) {
                score += 11;
                score += (numberAs-1);
            } else {
                score += numberAs;
            }
        }

        return score;
    }
    
    private void dealerFinish () {
        this.player.printMessages(this.showDealerCards());
        while (getScore(this.cards) < 17) {
            this.cards.add(this.hit());
            this.player.printMessages(this.showDealerCards());
        }
    }

    private void winner() {
        int dealer = this.getScore(this.cards);
        int player = this.getScore(this.player.getCards());
        String message = "";

        if ((dealer > 21 && player > 21) || (player == dealer)) {
            message = "Empate";
        } else if ((dealer > 21 && player <= 21) || (dealer <= 21 && player <= 21 && player > dealer)) {
            message = "Você ganhou :-7";
        } else {
            message = "A casa ganhou $_$";
        }

        this.player.printMessages("Dealer: " + dealer + "\nYou: " + player + "\n" + message);
    }
}
