package wtf.room;

import wtf.util.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomDispatcher implements Handler<RoomClient> {

    private final Room defaultRoom;
    private final Map<String, Room> roomsByName = new ConcurrentHashMap<>();
    private final Map<String, RoomCommandHandler> roomCommandHandlersByAlias = new ConcurrentHashMap<>();

    public RoomDispatcher() {
        defaultRoom = new Room(this, "default-room");
        roomsByName.put(defaultRoom.getRoomName(), defaultRoom);
    }

    public void addRoomCommandHandler(String alias, RoomCommandHandler roomCommandHandler) {
        roomCommandHandlersByAlias.put(alias, roomCommandHandler);
    }

    public RoomCommandHandler getRoomCommandHandlerByAlias(String alias) {
        return roomCommandHandlersByAlias.get(alias);
    }

    public Room getDefaultRoom() {
        return defaultRoom;
    }

    public Room createRoom(String name) {
        Room room = new Room(this, name);

        roomsByName.put(room.getRoomName(), room);
        return room;
    }

    public Room getRoomByName(String name) {
        return roomsByName.get(name);
    }

    @Override
    public void handle(RoomClient roomClient) {
        roomClient.enterRoom(defaultRoom);
    }
}
