import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        new Server(1200);
    }

    private int PORT;
    private int id = 1;
    
    public Server (int PORT) {
        this.PORT =  PORT;

        try {
            this.init();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    private void init() throws IOException {
        ServerSocket server = new ServerSocket(this.PORT);
        Dealer d = new Dealer();
        while (true) {
            try {
                Socket player = server.accept();
                
                Player p = new Player(player, id++);
                
                if (d.isAvailable()) {
                    d.addPlayer(p);
                } else {
                    d = new Dealer();
                }
                
            } catch (Exception e) {
                System.out.println(e);
                server.close();
            }
        }
    }
}

