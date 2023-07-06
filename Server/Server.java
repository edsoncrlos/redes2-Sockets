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
        while (true) {
            try {
                Socket player = server.accept();
                
                Player p = new Player(player, id++);
                new Dealer(p);
            } catch (Exception e) {
                System.out.println(e);
                server.close();
            }
        }
    }
}

