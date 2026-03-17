package dev.herrerao;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private final DataStorage database;

    public Server(DataStorage database) {
        this.database = database;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println("Listening on port: 8000");

                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Accepted connection from "
                            + socket.getInetAddress().getHostName());

                    new Thread(new HandleClient(socket)).start();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    class HandleClient implements Runnable {
        private final Socket socket;
        private User loggedInUser = null;

        public HandleClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
            ) {
                while (true) {
                    Object request = input.readObject();

                    if (!(request instanceof String line)) {
                        output.writeObject("ERROR: invalid request");
                        output.flush();
                        continue;
                    }

                    handleCommand(line.trim(), output, input);
                }

            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void handleCommand(String line, ObjectOutputStream output, ObjectInputStream input) throws Exception {
            if (line.startsWith("REGISTER ")) {
                String[] args = line.substring("REGISTER ".length()).split("\\|", 2);
                if (args.length < 2) {
                    output.writeObject("ERROR: Wrong number of arguments");
                    output.flush();
                    return;
                }

                boolean success = database.addUser(args[0], args[1]);
                output.writeObject(success ? "SUCCESS" : "FAILURE");
                output.flush();

            } else if (line.startsWith("LOGIN ")) {
                String[] args = line.substring("LOGIN ".length()).split("\\|", 2);
                if (args.length < 2) {
                    output.writeObject("ERROR: Wrong number of arguments");
                    output.flush();
                    return;
                }

                User user = database.getUser(args[0], args[1]);
                if (user != null) {
                    loggedInUser = user;
                    output.writeObject("SUCCESS " + user.id());
                } else {
                    output.writeObject("ERROR: wrong username or password");
                }
                output.flush();

            } else if (line.equals("INBOX")) {
                if (loggedInUser == null) {
                    output.writeObject("ERROR: not_logged_in");
                    output.flush();
                    return;
                }

                HashMap<Integer, ArrayList<Message>> messages =
                        database.getMessages(loggedInUser.id());

                if (messages.isEmpty()) {
                    output.writeObject("ERROR: no messages");
                    output.flush();
                    return;
                }

                output.writeObject(messages);
                output.flush();

            } else if (line.equals("SEND")) {
                if (loggedInUser == null) {
                    output.writeObject("ERROR: not_logged_in");
                    output.flush();
                    return;
                }

                output.writeObject("READY");
                Object readObject = input.readObject();

                if (readObject instanceof Message message) {
                    if (database.insertMessage(message)) {
                        output.writeObject("SENT");
                    } else {
                        output.writeObject("FAIL");
                    }
                } else {
                    output.writeObject("ERROR: wrong message data");
                }
            } else if (line.equals("GET_USER_ID")) {
                if (loggedInUser == null) {
                    output.writeObject("ERROR: not_logged_in");
                }

                Object readObject = input.readObject();

                if (readObject instanceof String username) {
                    output.writeObject(database.getUserID(username));
                }

            } else {
                output.writeObject("ERROR: unknown command");
                output.flush();
            }
        }
    }
}