import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

public class Dealer {
    private String[] typesCards = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private Stack<String> deck;
    private boolean isAvailable;
    private List<Player> players;
    private List<String> cards;

    Dealer () {
        this.deck = new Stack<String>();
        this.cards = new ArrayList<String>();
        
        this.players = new ArrayList<Player>();
        this.isAvailable = true;
    }
    
    public void newGame() {
        this.isAvailable = false;
        this.makeDeck();
        if (!this.cards.isEmpty()) {
            this.cards.clear();
        }
        this.startDealer();
        this.startPlayers();
    }

    public void addPlayer(Player player) {
        player.printMessages("Esperando por jogadores...\nPressione 0 para iniciar a partida.");
        this.players.add(player);
        this.printBroadcastMessages("player "+player.getId()+" entrou.");

        player.setDealer(this);
        new Thread(player).start();
    }

    private void startDealer() {
        this.cards.add(this.hit());
        this.cards.add(this.hit());
    }
    
    private void startPlayers() {
        for (Player player: this.players) {
            player.printMessages("\nDealer: " + this.cards.get(0) + " *");
            this.startPlayer(player);
            player.printMessages("\n1- Hit\n2- Stand\n3 - Sair");
        }
    }

    private void startPlayer(Player player) {
        player.newGame();
        player.hit(this.hit());
        player.hit(this.hit());
        player.showCurrentCards();
    }

    public int numberPlayers() {
        return this.players.size();
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
        message += ": " + this.getScore(this.cards);

        return message;
    }

    public void removePlayer(Player player) {
        this.printBroadcastMessages("Player "+ player.getId() + " saiu.", player.getId());
        this.players.remove(player);

        if (!this.isAvailable) {
            if (this.allStand()) {
                this.finishMatch();
            }
        }
    }
    
    public String hit() {
        try {
            return this.deck.pop();
        } catch (EmptyStackException e) {
            throw e;
        }
    }

    private boolean allStand() {
        for (Player player: this.players) {
            if (!player.getStand()) {
                return false;
            }
        }
        return true;
    }

    public void stand(Player player) {      
        if (this.allStand()) {
            this.finishMatch();
        } else {
            player.clearScreen();
            player.printMessages("Esperando outros jogadores...");
        }
    }

    public void finishMatch() {
        this.dealerFinish();
        this.winnerMessage();
        
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        this.isAvailable = true;
        this.printBroadcastMessages("\nEsperando por jogadores...\nPressione 0 para iniciar a partida.");
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
        for (Player player: this.players) {
            player.clearScreen();
        }

        try {
            while (getScore(this.cards) < 17) {
                this.cards.add(this.hit());
            }
            this.printBroadcastMessages(this.showDealerCards());
        } catch (EmptyStackException e) {
            this.printBroadcastMessages("Acabaram as cartas");
        }
    }

    private void winnerMessage() {
        this.winner();
    }

    public void winner() {
        int dealer = this.getScore(this.cards);
        int[] playersScore = new int[this.players.size()];
        int higher = 0;
        int numberHigher = 1;

        for (int i = 0; i < this.players.size(); i++) {
            playersScore[i] = this.getScore(this.players.get(i).getCards());
            if (playersScore[i] > higher && playersScore[i] <= 21) {
                higher = playersScore[i];
                numberHigher = 1;
            } else if (playersScore[i] == higher) {
                numberHigher++;
            }
        }

        // Dealer
        if (dealer > higher && dealer <= 21) {
            higher = dealer;
            if (numberHigher >= 1) {
                numberHigher = 0;
            }
        } else if (dealer == higher) {
            numberHigher++;
        } 

        for (Player player: this.players) {
            player.showCurrentCards();
        }
        for (Player player: this.players) {
            this.printBroadcastMessages(player.currentCards("Player "+ player.getId()+ ":"), player.getId());
        }
        
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);

            if (numberHigher == 1 && higher == playersScore[i]) {
                player.printMessages("Você ganhou");
            } else if (numberHigher > 1 && higher == playersScore[i]) {
                player.printMessages("Você Empatou");
            } else {
                player.printMessages("Você perdeu");
            } 
            
            if (numberHigher == 0) {
                player.printMessages("A casa ganhou  $_$");
            }
        }
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    private void printBroadcastMessages(String message) {
        for (Player player: this.players) {
            player.printMessages(message);
        }
    }

    private void printBroadcastMessages(String message, int notPrintForPlayer) {
        for (Player player: this.players) {
            if (player.getId() != notPrintForPlayer) {
                player.printMessages(message);
            }
        }
    }
}
