import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        new Server(1200);
    }
    private int PORT;
    private PrintStream writePlayers;
    private ServerSocket server;
    
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
            Socket player = server.accept();
            System.out.println("Jogador 1 entrou");

            PrintStream playerWrite = new PrintStream(player.getOutputStream());
            this.writePlayers = playerWrite;
            
            PlayerThreadRead playerThreadRead = new PlayerThreadRead(player.getInputStream(), this);
            new Thread(playerThreadRead).start();
        }
        // server.close();
    }

    public void printMessageClient(String message) {
        this.writePlayers.println("Player1: " + message);
    }
}

class PlayerThreadRead implements Runnable {
    private InputStream client;
    private Server server;

    PlayerThreadRead (InputStream client, Server server) {
        this.client = client;
        this.server = server;
    }

    public void run() {
        Scanner s = new Scanner(this.client);

        while (s.hasNextLine()) {
            this.server.printMessageClient(s.nextLine());
        }
        s.close();
    }
}