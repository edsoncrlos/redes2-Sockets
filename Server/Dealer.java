import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

public class Dealer {
    private String[] typesCards = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private Stack<String> deck;
    private Player player;
    private List<String> cards;

    Dealer (Player player) {
        this.deck = new Stack<String>();
        this.cards = new ArrayList<String>();
        
        this.player = player;
        this.addPlayer(player);

        this.newGame();
    }
    
    public void newGame() {
        this.makeDeck();
        if (!this.cards.isEmpty()) {
            this.cards.clear();
        }
        this.startDealer();
        this.startPlayer();
    }

    public void addPlayer(Player player) {
        player.setDealer(this);
        new Thread(player).start();
        // thread.start
    }

    private void startDealer() {
        this.cards.add(this.hit());
        this.cards.add(this.hit());
    }
    
    private void startPlayer() {
        this.player.printMessages("Dealer: " + this.cards.get(0) + " *");
    
        this.player.newGame();
        this.player.hit(this.hit());
        this.player.hit(this.hit());
        this.player.showCurrentCards();
        
        this.player.printMessages("1- Hit\n2- Stand\n3 - Placar");
    }


    private void makeDeck () {
        if (!this.deck.empty())
            this.deck.clear();

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
        try {
            return this.deck.pop();
        } catch (EmptyStackException e) {
            throw e;
        }
    }
    
    public void stand() {
        this.player.stand();
        this.player.clearScreen();

        this.dealerFinish();
        this.winnerMessage();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        this.player.printMessages("\n");
        this.newGame();
    }

    public int getScore(List<String> cards) {
        int numberAce = 0;
        int score = 0;

        for (int i = 0; i < cards.size(); i++) {
            String card = cards.get(i);
            if (card == "J" || card == "Q" || card == "K") {
                score += 10;
            } else if (card == "A") {
                numberAce += 1;
            } else {
                score += Integer.parseInt(card);
            }
        }
        if (numberAce > 0) {
            // check if value of ace can be 11 
            if (score + 11 + (numberAce - 1) <= 21) {
                score += 11;
                score += (numberAce-1);
            } else {
                score += numberAce;
            }
        }

        return score;
    }
    
    private void dealerFinish () {
        this.player.printMessages(this.showDealerCards());
        try {
            while (getScore(this.cards) < 17) {
                this.cards.add(this.hit());
                this.player.printMessages(this.showDealerCards());
            }
        } catch (EmptyStackException e) {
            this.player.printMessages("Acabaram as cartas");
        }
    }

    private void winnerMessage() {
        int dealer = this.getScore(this.cards);
        int player = this.getScore(this.player.getCards());
        String winner = this.winner();
        String message;

        if (winner == "Dealer") {
            message = "A casa ganhou $_$";
        } else if (winner == "Player") {
            message = "Você ganhou :-7";
        } else {
            message = "Empate";
        }

        this.player.printMessages("Dealer: " + dealer + "\nYou: " + player + "\n" + message);
    }

    public String winner() {
        int dealer = this.getScore(this.cards);
        int player = this.getScore(this.player.getCards());
        String message;

        if ((dealer > 21 && player > 21) || (player == dealer)) {
            message = "Empate";
        } else if ((dealer > 21 && player <= 21) || (dealer <= 21 && player <= 21 && player > dealer)) {
            message = "Player";
        } else {
            message = "Dealer";
        }

        return message;
    }
}
