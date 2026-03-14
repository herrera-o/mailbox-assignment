package dev.herrerao;

import org.hsqldb.Database;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private DataStorage database;

    public Server(DataStorage database) {
        this.database = database;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocker = new ServerSocket(8000);
                System.out.println("Listening on port: 8000");

                while (true) {
                    Socket socket = serverSocker.accept();
                    System.out.println("Accepted connection from " + socket.getInetAddress().getHostName());

                    new Thread(new HandleClient(socket)).start();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    class HandleClient implements Runnable {
        private Socket socket;

        public HandleClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(in));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    output.println("Welcome to mailbox.");
                    output.print("Enter username: ");
                    output.flush();
                    String username = input.readLine();
                    output.print("Enter password: ");
                    output.flush();
                    String password = input.readLine();

                    output.println("You entered: " + username + " | " + password);
                    // login


                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
