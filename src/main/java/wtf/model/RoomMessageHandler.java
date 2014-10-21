package wtf.model;

public interface RoomMessageHandler {

    void onRoomClientRegistered(RoomClient roomClient, Room room);

    void onRoomClientUnregistered(RoomClient roomClient, Room room);

    void handleMessage(RoomClient roomClient, String message);
}
