package wtf.model;

import wtf.model.command.BroadcastCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class RoomCommandHandler {

    private final RoomCommand defaultRoomCommand;
    private final Map<String, RoomCommand> roomCommandsByAlias = new ConcurrentHashMap<>();
    private final MessageHistoryHolder messageHistoryHolder = new MessageHistoryHolder(100);

    public RoomCommandHandler() {
        this.defaultRoomCommand = new BroadcastCommand(messageHistoryHolder);
    }

    public void onRoomClientRegistered(RoomClient roomClient, Room room) {
        messageHistoryHolder.getLastMessagesFor(room).forEach(roomClient::write);
        announce(room, roomClient.getName() + " entered " + room.getRoomName());
    }

    public void onRoomClientUnregistered(RoomClient roomClient, Room room) {
        announce(room, roomClient.getName() + " leaved " + room.getRoomName());
    }

    private void announce(Room room, String message) {
        room.getRoomClients().values().forEach(rc -> rc.write(message));
        messageHistoryHolder.storeMessageFor(room, message);
    }

    public void addRoomCommand(String alias, RoomCommand roomCommand) {
        roomCommandsByAlias.put(alias, roomCommand);
    }

    public void handleMessage(RoomClient roomClient, String message) {
        Scanner scanner = new Scanner(message);
        String alias = scanner.next();
        RoomCommand handler = roomCommandsByAlias.get(alias);
        if (handler != null) {
            handleMessage(roomClient, scanner, handler);
        } else {
            defaultRoomCommand.execute(roomClient, message);
        }
    }

    private static void handleMessage(RoomClient roomClient, Scanner scanner, RoomCommand handler) {
        List<String> args = new ArrayList<>();
        while (scanner.hasNext()) {
            args.add(scanner.next());
        }
        handler.execute(roomClient, args.toArray(new String[args.size()]));
    }
}
