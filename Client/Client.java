import java.io.InputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int port;
    private String host = "127.0.0.0";
    
    public static void main(String[] args) {
        new Client("127.0.0.1", 1200);
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
                String message = s.nextLine();
                clientWrite.println(message);

                if (client.isClosed()) {
                    System.out.println("Conexão perdida com o servidor.");
                    break;
                }
                
                if (message.trim().equals("4")) {
                    System.out.println("Tchau!!");
                    break;
                }
            }

            s.close();
            client.close();
        } catch (ConnectException e) {
            System.out.println("Não possível conectar conectar com o servidor");
        } catch (NoRouteToHostException e) {
            System.out.println("O servidor não está disponível ou seu endereço está incorreto.");
        } catch (Exception e) {
            System.out.println(e);
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