import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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
                
                PrintStream playerWrite = new PrintStream(player.getOutputStream());
                InputStream playerOutput = player.getInputStream();
                Player p = new Player(playerWrite, playerOutput, id++);

                new Dealer(p);
            } catch (Exception e) {
                System.out.println(e);
                server.close();
            }
        }
    }
}

