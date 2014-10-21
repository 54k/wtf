package wtf.model;

public interface ClientSessionListener {

    void onMessage(String message);

    void onDisconnect();
}
