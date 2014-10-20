package wtf.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private final Map<String, RoomClient> roomClientsByName = new ConcurrentHashMap<>();
    private final String roomName;
    private final Lobby lobby;

    Room(Lobby lobby, String roomName) {
        this.lobby = lobby;
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public Map<String, RoomClient> getRoomClients() {
        return Collections.unmodifiableMap(roomClientsByName);
    }

    void addRoomClient(RoomClient roomClient) {
        roomClientsByName.put(roomClient.getName(), roomClient);
    }

    void removeRoomClient(RoomClient roomClient) {
        roomClientsByName.remove(roomClient.getName(), roomClient);
    }
}
