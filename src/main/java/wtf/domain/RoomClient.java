package wtf.domain;

public interface RoomClient extends ClientSession {

    Room getCurrentRoom();

    void enterRoom(Room room);
}
