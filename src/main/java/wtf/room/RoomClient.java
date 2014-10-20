package wtf.room;

import wtf.net.NetworkChannel;

public class RoomClient {

    private final String name;
    private final NetworkChannel networkChannel;
    private volatile Room currentRoom;

    RoomClient(String name, NetworkChannel networkChannel) {
        this.name = name;
        this.networkChannel = networkChannel;
        init();
    }

    private void init() {
        networkChannel.onClose(v -> leaveRoom());
        networkChannel.onMessage(this::onMessage);
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void close() {
        networkChannel.close();
    }

    public void write(String msg) {
        networkChannel.write(msg);
    }

    private void onMessage(String message) {
        if (currentRoom != null) {
            currentRoom.handleMessage(this, message);
        }
    }

    public void enterRoom(Room room) {
        if (this.currentRoom != null) {
            leaveRoom();
        }
        room.addRoomClient(this);
        this.currentRoom = room;
    }

    public void leaveRoom() {
        if (currentRoom != null) {
            currentRoom.removeRoomClient(this);
        }
        currentRoom = null;
    }
}
