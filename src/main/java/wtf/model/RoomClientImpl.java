package wtf.model;

public class RoomClientImpl implements RoomClient, ClientSessionListener {

    private ClientSession clientSession;
    private Room currentRoom;
    private RoomMessageHandler messageHandler;

    public RoomClientImpl(ClientSession clientSession, RoomMessageHandler messageHandler) {
        this.clientSession = clientSession;
        this.messageHandler = messageHandler;
    }

    @Override
    public Room getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public void enterRoom(Room room) {
        if (this.currentRoom != null) {
            leaveRoom();
        }
        room.addRoomClient(this);
        messageHandler.onRoomClientRegistered(this, room);
        this.currentRoom = room;
    }

    private void leaveRoom() {
        if (currentRoom != null) {
            currentRoom.removeRoomClient(this);
            messageHandler.onRoomClientUnregistered(this, currentRoom);
            currentRoom = null;
        }
    }

    @Override
    public String getName() {
        return clientSession.getName();
    }

    @Override
    public void write(String msg) {
        clientSession.write(msg);
    }

    @Override
    public void close() {
        clientSession.close();
    }

    @Override
    public void onMessage(String message) {
        if (currentRoom != null) {
            messageHandler.handleMessage(this, message);
        }
    }

    @Override
    public void onDisconnect() {
        leaveRoom();
    }
}
