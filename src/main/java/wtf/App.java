package wtf;

import wtf.model.Application;
import wtf.model.Authenticator;
import wtf.net.NetworkServer;

public class App {

    public static void main(String[] args) {
        NetworkServer networkServer = new NetworkServer();
        networkServer.onConnection(new Authenticator(new Application()));
        networkServer.bind(8080);
    }
}
