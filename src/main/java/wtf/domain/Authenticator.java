package wtf.domain;

import wtf.kernel.network.NetworkSession;
import wtf.util.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Authenticator implements Handler<NetworkSession> {

    private final Map<String, ClientSession> clientSessionByName;
    private final LoginListener loginListener;

    public Authenticator(LoginListener loginListener) {
        this.loginListener = loginListener;
        clientSessionByName = new ConcurrentHashMap<>();
    }

    @Override
    public void handle(NetworkSession networkChannel) {
        networkChannel.send("Please enter your name");
        networkChannel.setMessageHandler(msg -> handleCredentials(networkChannel, msg));
    }

    private void handleCredentials(NetworkSession networkChannel, String credentials) {
        if (clientSessionByName.containsKey(credentials)) {
            networkChannel.send("Name already exists, please try again");
            return;
        }
        handleLogIn(networkChannel, credentials);
    }

    private void handleLogIn(NetworkSession networkChannel, String credentials) {
        ClientSessionImpl clientSession = new ClientSessionImpl(credentials, networkChannel);
        clientSessionByName.put(credentials, clientSession);
        ClientSessionListener clientSessionListener = loginListener.onLogin(clientSession);
        networkChannel.setMessageHandler(clientSessionListener::onMessage);
        networkChannel.setCloseHandler(v -> handleDisconnect(credentials, clientSessionListener));
    }

    private void handleDisconnect(String credentials, ClientSessionListener clientSessionListener) {
        clientSessionByName.remove(credentials);
        clientSessionListener.onDisconnect();
    }
}
