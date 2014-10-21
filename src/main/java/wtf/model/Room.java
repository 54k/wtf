package wtf.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private Map<String, RoomClient> roomClientsByName = new ConcurrentHashMap<>();
    private String roomName;
    private Lobby lobby;

    public Room(Lobby lobby, String roomName) {
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

    public void addRoomClient(RoomClient roomClient) {
        roomClientsByName.put(roomClient.getName(), roomClient);
    }

    public void removeRoomClient(RoomClient roomClient) {
        roomClientsByName.remove(roomClient.getName(), roomClient);
    }
}
