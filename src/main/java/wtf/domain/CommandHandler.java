package wtf.domain;

public interface CommandHandler {

    void handleMessage(RoomClient roomClient, String message);
}
