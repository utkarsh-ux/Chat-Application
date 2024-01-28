import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

/**
 * This class represents a simple client that connects to a server and enables communication with it.
 */
public class Client {
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    /**
     * Constructor for the Client class. It establishes a connection to the server specified by the IP address
     * and port number and initializes input and output streams for communication.
     */
    public Client() {
        try {
            System.out.println("Sending request to the server");
            socket = new Socket("10.35.1.13", 7777); // Connect to the server at IP address 127.0.0.1 and port 7777
            System.out.println("Connection established...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading(); // Start the thread for reading messages from the server
            startWriting(); // Start the thread for sending messages to the server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to start the reading thread. This thread reads messages from the server.
     */
    public void startReading() {
        // Thread for reading messages from the server
        Runnable r1 = () -> {
            System.out.println("Reader started");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection is closed...");
            }
        };
        new Thread(r1).start();
    }

    /**
     * Method to start the writing thread. This thread allows the client to send messages to the server.
     */
    public void startWriting() {
        // Thread for sending messages to the server
        Runnable r2 = () -> {
            System.out.println("Writing started");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection is closed...");
            }
        };
        new Thread(r2).start();
    }

    /**
     * The main method of the Client class. It creates an instance of the client and starts it.
     *
     */
    public static void main(String[] args) {
        System.out.println("Client is starting...");
        new Client();
    }
}
