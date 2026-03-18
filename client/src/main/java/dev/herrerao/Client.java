package dev.herrerao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private HashMap<Integer, ArrayList<Message>> inbox;
    private HashMap<Integer, User> users;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;

        users = new HashMap<>();
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);

        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(socket.getInputStream());

        System.out.println("Connected to " + host + ":" + port);
    }

    public void close() {
        try {
            if (input != null) { input.close(); }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (output != null) { output.close(); }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (socket != null) { socket.close(); }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Closed connection to " + host + ":" + port);
    }

    public Object sendCommand(String command) throws Exception {
        output.writeObject(command);
        output.flush();

        return input.readObject();
    }

    static void main() {
        Client client = new Client("localhost", 8000);
        Scanner sc = new Scanner(System.in);

        try {
            client.connect();
            boolean running = true;

            System.out.println("___ client mailbox ___");

            while (running) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. View Inbox");
                System.out.println("4. Send");
                System.out.println("5. Quit");
                System.out.print("Choose an option: ");

                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1" -> {
                        System.out.print("Enter username: ");
                        String username = sc.nextLine().trim();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine().trim();

                        String command = "REGISTER " + username + "|" + password;
                        Object response = client.sendCommand(command);

                        if (response instanceof String message) {
                            System.out.println("Server: " + message);
                        } else {
                            System.out.println("Error: something went wrong");
                        }
                    }

                    case "2" -> {
                        System.out.print("Enter username: ");
                        String username = sc.nextLine().trim();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine().trim();

                        String command = "LOGIN " + username + "|" + password;
                        Object response = client.sendCommand(command);

                        if (response instanceof String message) {
                            System.out.println("Server: " + message);
                        } else {
                            System.out.println("Error: something went wrong");
                        }
                    }

                    case "3" -> {
                        Object response = client.sendCommand("INBOX");

                        if (response instanceof String message) {
                            System.out.println("Server: " + message);
                        } else if (response instanceof HashMap<?, ?> map) {
                            client.inbox = (HashMap<Integer, ArrayList<Message>>) map;
                        }
                    }

                    case "4" -> {
                        System.out.print("Enter recipient: ");
                        String recipient = sc.nextLine().trim();



                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
            sc.close();
        }

    }
}
