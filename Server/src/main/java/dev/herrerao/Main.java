package dev.herrerao;

public class Main {
    public static void main(String[] args) {
        try {

            Server srv = new Server(new DataStorage());
            srv.startServer();

        } catch (Exception e) {

        }

    }
}
