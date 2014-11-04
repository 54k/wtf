package wtf.domain;

public interface RoomClient extends ClientSession {

    Room getCurrentRoom();

    void joinRoom(Room room);

    void leaveRoom();
}
