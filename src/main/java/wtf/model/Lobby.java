package wtf.model;

import wtf.util.Handler;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby implements Handler<RoomClient> {

    private final Room defaultRoom;
    private final Map<String, Room> roomsByName = new ConcurrentHashMap<>();

    public Lobby() {
        defaultRoom = new Room(this, "default-room");
        roomsByName.put(defaultRoom.getRoomName(), defaultRoom);
    }

    public Room getDefaultRoom() {
        return defaultRoom;
    }

    public Room createRoom(String name) {
        if (roomsByName.containsKey(name)) {
            return getRoomByName(name);
        }
        Room room = new Room(this, name);
        roomsByName.put(room.getRoomName(), room);
        return room;
    }

    public Room getRoomByName(String name) {
        return roomsByName.get(name);
    }

    public Map<String, Room> getRooms() {
        return Collections.unmodifiableMap(roomsByName);
    }

    @Override
    public void handle(RoomClient roomClient) {
        roomClient.enterRoom(defaultRoom);
    }
}
