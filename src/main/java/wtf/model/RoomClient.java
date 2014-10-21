package wtf.model;

public interface RoomClient extends ClientSession {

    Room getCurrentRoom();

    void enterRoom(Room room);
}
