import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int port;
    private String host = "127.0.0.1";
    
    public static void main(String[] args) {
        new Client(null, 1200);
    }
    
    public Client (String host, int port) {
        this.host = host;
        this.port = port;
        this.init();
    }
    
    private void init() {
        try {
            Socket client = new Socket(this.host, this.port);
            
            ClientReceiver clientRead = new ClientReceiver(client.getInputStream());
            new Thread(clientRead).start();

            PrintStream clientWrite = new PrintStream(client.getOutputStream());
            
            Scanner s = new Scanner(System.in);
            while (s.hasNextLine()) {
                clientWrite.println(s.nextLine());
            }

            s.close();
            client.close();
        } catch (Exception e) {
            // TODO: handle exception
        }  
    }
}

class ClientReceiver implements Runnable {
    private InputStream server;

    ClientReceiver (InputStream server) {
        this.server = server;
    }

    public void run() {
        Scanner s = new Scanner(this.server);

        while(s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
        s.close();
    }
}