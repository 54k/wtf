package wtf.domain;

import wtf.domain.command.BroadcastCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class CommandHandlerImpl implements CommandHandler, RoomEventListener {

    private final Command defaultCommand;
    private final Map<String, Command> roomCommandsByAlias = new ConcurrentHashMap<>();
    private final MessageHistoryHolder messageHistoryHolder = new MessageHistoryHolder(100);

    public CommandHandlerImpl() {
        this.defaultCommand = new BroadcastCommand(messageHistoryHolder);
    }

    @Override
    public void onRoomClientRegistered(RoomClient roomClient, Room room) {
        messageHistoryHolder.getLastMessagesFor(room).forEach(roomClient::write);
        announce(room, roomClient.getName() + " entered " + room.getRoomName());
    }

    @Override
    public void onRoomClientUnregistered(RoomClient roomClient, Room room) {
        announce(room, roomClient.getName() + " leaved " + room.getRoomName());
    }

    private void announce(Room room, String message) {
        room.getRoomClients().values().forEach(rc -> rc.write(message));
        messageHistoryHolder.storeMessageFor(room, message);
    }

    public void addCommand(String alias, Command command) {
        roomCommandsByAlias.put(alias, command);
    }

    @Override
    public void handleMessage(RoomClient roomClient, String message) {
        Scanner scanner = new Scanner(message);
        String alias = scanner.next();
        Command handler = roomCommandsByAlias.get(alias);
        if (handler == null) {
            defaultCommand.execute(roomClient, message);
        } else {
            handleMessage(roomClient, scanner, handler);
        }
    }

    private static void handleMessage(RoomClient roomClient, Scanner scanner, Command handler) {
        List<String> args = new ArrayList<>();
        while (scanner.hasNext()) {
            args.add(scanner.next());
        }

        try {
            handler.execute(roomClient, args.toArray(new String[args.size()]));
        } catch (Throwable t) {
            // TODO properly handle exception
        }
    }
}
