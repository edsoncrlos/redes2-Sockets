import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        new Server(1200);
    }

    private int PORT;
    private int id = 1;
    private List<Dealer> dealers;

    static int MAX_NUMBER_PLAYERS_FOR_MATCH = 5;
    static int MAX_NUMBER_DEALERS_WITHOUT_PLAYER = 5 ;
    
    public Server (int PORT) {
        this.PORT =  PORT;
        this.dealers = new LinkedList<Dealer>();

        try {
            this.init();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    private void init() throws IOException {
        ServerSocket server = new ServerSocket(this.PORT);
        while (true) {
            try {
                Socket player = server.accept();

                Player p = new Player(player, id++);
                
                Dealer d = this.getDealerAvaliable();
                if (d != null && d.numberPlayers() <= MAX_NUMBER_PLAYERS_FOR_MATCH) {
                    d.addPlayer(p);
                    this.cleanDealers();
                } else {
                    d = new Dealer();
                    this.dealers.add(d);
                    d.addPlayer(p);
                }
            } catch (Exception e) {
                System.out.println(e);
                server.close();
            }
        }
    }

    private Dealer getDealerAvaliable() {
        for (Dealer dealer: this.dealers) {
            if (dealer.isAvailable()) {
                return dealer;
            }
        }
        return null;
    }

    private void cleanDealers() {
        if (this.dealers.size() > MAX_NUMBER_DEALERS_WITHOUT_PLAYER) {
            for (Dealer dealer: this.dealers) {
                if (dealer.numberPlayers() == 0) {
                    this.dealers.remove(dealer);
                }
            }
        }
    }
}

