import java.net.*;
import java.io.*;

/**
 * This class represents a simple server that accepts incoming client connections and allows
 * communication with clients.
 */
public class Server {
    // SocketServer
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    /**
     * Constructor for the Server class. It initializes the server socket and sets up input and output
     * streams for communication.
     */
    public Server() {
        try {
            // Create a server socket on port 7777
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connections");
            System.out.println("Waiting...");

            // Accept a client connection
            socket = server.accept();

            // Create input and output streams for communication with the client
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            // Start reading and writing threads
            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to start the reading thread. This thread reads messages from the client.
     */
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection is closed...");
            }
        };
        new Thread(r1).start();
    }

    /**
     * Method to start the writing thread. This thread allows the server to send messages to the client.
     */
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writing started");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    // Send user input to the client
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
     * The main method of the Server class. It creates an instance of the server and starts it.
     *
     * @param args The command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        System.out.println("Server is starting...");
        new Server();
    }
}
