package dev.herrerao;

public class Main {
    static void main() {
        try {

            Server srv = new Server(new DataStorage());
            srv.startServer();

        } catch (Exception e) {

        }

    }
}
