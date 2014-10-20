package wtf.model;

import wtf.net.NetworkChannel;

public class RoomClient {

    private final String name;
    private final NetworkChannel networkChannel;
    private final RoomCommandHandler roomCommandHandler;
    private volatile Room currentRoom;

    public RoomClient(String name, NetworkChannel networkChannel, RoomCommandHandler roomCommandHandler) {
        this.name = name;
        this.networkChannel = networkChannel;
        this.roomCommandHandler = roomCommandHandler;
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
            roomCommandHandler.handleMessage(this, message);
        }
    }

    public void enterRoom(Room room) {
        if (this.currentRoom != null) {
            leaveRoom();
        }
        room.addRoomClient(this);
        roomCommandHandler.onRoomClientRegistered(this, room);
        this.currentRoom = room;
    }

    public void leaveRoom() {
        if (currentRoom != null) {
            currentRoom.removeRoomClient(this);
            roomCommandHandler.onRoomClientUnregistered(this, currentRoom);
        }
        currentRoom = null;
    }
}
