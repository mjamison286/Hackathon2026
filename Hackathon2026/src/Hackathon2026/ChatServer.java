package Hackathon2026;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
    private static List<PrintWriter> clients = new ArrayList<>();

    public static boolean initialized = false;

    public static void initialize(){
        try (ServerSocket server = new ServerSocket(5000))
        {
            System.out.println("Server is starting");
            initialized = true;
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client connected");

                new Thread(() -> handleClient(socket)).start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            clients.add(out);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);

                for (PrintWriter client : clients) {
                    client.println(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
