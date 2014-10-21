package wtf.model;

import wtf.net.NetworkChannel;
import wtf.util.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Authenticator implements Handler<NetworkChannel> {

    private Map<String, ClientSession> clientSessionByName = new ConcurrentHashMap<>();
    private ApplicationListener applicationListener;

    public Authenticator(ApplicationListener applicationListener) {
        this.applicationListener = applicationListener;
    }

    @Override
    public void handle(NetworkChannel networkChannel) {
        networkChannel.write("Please enter your name");
        networkChannel.onMessage(msg -> handleCredentials(networkChannel, msg));
    }

    private void handleCredentials(NetworkChannel networkChannel, String credentials) {
        if (clientSessionByName.containsKey(credentials)) {
            networkChannel.write("Name already exists, please try again");
            return;
        }
        handleLogIn(networkChannel, credentials);
    }

    private void handleLogIn(NetworkChannel networkChannel, String credentials) {
        ClientSessionImpl clientSession = new ClientSessionImpl(credentials, networkChannel);
        clientSessionByName.put(credentials, clientSession);
        ClientSessionListener clientSessionListener = applicationListener.onLogIn(clientSession);
        networkChannel.onMessage(clientSessionListener::onMessage);
        networkChannel.onClose(v -> handleDisconnect(credentials, clientSessionListener));
    }

    private void handleDisconnect(String credentials, ClientSessionListener clientSessionListener) {
        clientSessionByName.remove(credentials);
        clientSessionListener.onDisconnect();
    }
}
