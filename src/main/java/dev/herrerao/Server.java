package dev.herrerao;

import org.hsqldb.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Database database;

    public Server(Database database) {
        this.database = database;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocker = new ServerSocket(8000);

                while (true) {
                    Socket socket = serverSocker.accept();
                    System.out.println("Accepted connection from " + socket.getInetAddress().getHostName());

                    new Thread(new HandleClient(socket)).start();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    class HandleClient implements Runnable {
        private Socket socket;

        public HandleClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                while(true) {
                    String command = inputFromClient.readUTF();
                    System.out.println(command);

                    // login

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
