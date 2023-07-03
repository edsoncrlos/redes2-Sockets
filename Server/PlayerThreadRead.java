import java.io.InputStream;
import java.util.Scanner;

class PlayerThreadRead implements Runnable {
    private InputStream client;
    private Player player;
    private Dealer dealer;

    PlayerThreadRead (InputStream client, Player player, Dealer dealer) {
        this.client = client;
        this.player = player;
        this.dealer = dealer;
    }

    public void run() {
        Scanner s = new Scanner(this.client);
        
        while (s.hasNextLine()) {
            try {
                int clientMessage = Integer.parseInt(s.nextLine().trim());

                if (clientMessage == 1) {
                    this.player.hit(this.dealer.hit());
                    this.player.showCurrentCards();
                }
                if (clientMessage == 2) {
                    this.dealer.stand();
                }

            } catch (NumberFormatException e) {
                this.player.printMessages("Somente números.");
            }
            
        }
        s.close();
    }
}