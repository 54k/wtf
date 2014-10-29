package wtf.domain;

import wtf.kernel.EventBus;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private final Map<String, RoomClient> roomClientsByName;
    private final String roomName;
    private final Lobby lobby;

    @Inject
    private EventBus eventBus;

    Room(Lobby lobby, String roomName) {
        this.lobby = lobby;
        this.roomName = roomName;
        roomClientsByName = new ConcurrentHashMap<>();
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
        eventBus.post(RoomEventListener.class).onRoomClientRegistered(roomClient, this);
    }

    public void removeRoomClient(RoomClient roomClient) {
        roomClientsByName.remove(roomClient.getName(), roomClient);
        eventBus.post(RoomEventListener.class).onRoomClientUnregistered(roomClient, this);
    }
}
