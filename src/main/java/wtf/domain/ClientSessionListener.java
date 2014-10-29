package wtf.domain;

public interface ClientSessionListener {

    void onMessage(String message);

    void onDisconnect();
}
