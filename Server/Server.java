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
            
            PrintStream playerWrite = new PrintStream(player.getOutputStream());
            InputStream playerOutput = player.getInputStream();
            Player p = new Player(playerWrite, playerOutput, id++);
            Dealer dealer =  new Dealer(p);
            // this.players.add(p);
            // this.broadcastMessages("Player: " + p.getId() + " entrou.");
            // this.sendMessage("1- Pedir carta\n2- Parar", p);
        }
        // server.close();
    }

    /*public void broadcastMessages(String message) {
        for (Player p: this.players) {
            p.printMessages(message);
        }
        // this.writePlayers.println("Player1: " + message);
    }

    public void sendMessage(String message, Player player) {
        player.printMessages(message);
    }*/
}

