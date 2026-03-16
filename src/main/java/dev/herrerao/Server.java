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
                    String command;
                    output.println("Welcome to mailbox.");
                    output.println("Existing User? [Y]es [N]o");
                    command = input.readLine();
                    
                    
                    switch (command) {
                        case "Y":
                            if (logIn(input, output)) {
                                userMenu(input, output);
                            } else {
                                output.println("Wrong username or password!");
                            }
                            break;
                        case "N" :
                        case "n":
                        case "No":
                        case "no":
                            createUser(input, output);
                            output.flush();
                            output.println("User created!");
                            break;
                        default:
                            output.println("Invalid command.");
                    }
                    
                    


                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void createUser(BufferedReader input, PrintWriter output) throws Exception {
            String username;
            String password;

            output.println("---Create User---");
            output.print("New username: ");
            output.flush();
            username = input.readLine();
            output.print("New password: ");
            output.flush();
            password = input.readLine();

            database.addUser(username, password);
        }

        private void userMenu(BufferedReader input, PrintWriter output)  {

        }

        private boolean logIn(BufferedReader input, PrintWriter output) throws IOException {
            output.print("Enter username: ");
            output.flush();
            String username = input.readLine();
            output.print("Enter password: ");
            output.flush();
            String password = input.readLine();

            output.println("You entered: " + username + " | " + password);
            // login

            return false;
        }
    }
}
