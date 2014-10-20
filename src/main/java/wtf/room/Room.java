package wtf.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private final Map<String, RoomClient> roomClientsByName = new ConcurrentHashMap<>();
    private final String roomName;
    private final RoomDispatcher roomDispatcher;

    Room(RoomDispatcher roomDispatcher, String roomName) {
        this.roomDispatcher = roomDispatcher;
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public RoomDispatcher getRoomDispatcher() {
        return roomDispatcher;
    }

    public void addRoomClient(RoomClient roomClient) {
        roomClientsByName.put(roomClient.getName(), roomClient);
        broadcast(roomClient.getName() + " entered " + roomName);
    }

    public void removeRoomClient(RoomClient roomClient) {
        roomClientsByName.remove(roomClient.getName(), roomClient);
        broadcast(roomClient.getName() + " leaved " + roomName);
    }

    public void handleMessage(RoomClient roomClient, String message) {
        if (roomClientsByName.containsKey(roomClient.getName())) {
            Scanner scanner = new Scanner(message);
            String alias = scanner.next();
            RoomCommandHandler handler = roomDispatcher.getRoomCommandHandlerByAlias(alias);
            if (handler != null) {
                List<String> args = new ArrayList<>();
                while (scanner.hasNext()) {
                    args.add(scanner.next());
                }
                handler.handle(roomClient, args.toArray(new String[args.size()]));
            } else {
                roomClientsByName.values().forEach(rc -> rc.write("Message from " + roomClient.getName() + ": " + message));
            }
        }
    }

    private void broadcast(String message) {
        roomClientsByName.values().forEach(rc -> rc.write(message));
    }
}
