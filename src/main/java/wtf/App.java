package wtf;

import wtf.domain.ChatApplication;
import wtf.kernel.Application;

import java.util.concurrent.ExecutionException;

public class App {

    private static final int DEFAULT_SERVER_PORT = 3000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int port = determineServerPort();
        new Application(new ChatApplication()).start(port).get();
        System.out.println("Application running on port: " + port);
    }

    private static int determineServerPort() {
        try {
            return Integer.parseInt(System.getProperty("serverPort"));
        } catch (NumberFormatException ignore) {
            return DEFAULT_SERVER_PORT;
        }
    }
}
