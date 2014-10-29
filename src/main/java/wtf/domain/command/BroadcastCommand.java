package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.MessageHistoryHolder;
import wtf.domain.Room;
import wtf.domain.RoomClient;

public class BroadcastCommand implements Command {

    private final MessageHistoryHolder messageHistoryHolder;

    public BroadcastCommand(MessageHistoryHolder messageHistoryHolder) {
        this.messageHistoryHolder = messageHistoryHolder;
    }

    @Override
    public void execute(RoomClient roomClient, String... args) {
        String msg = "Message from " + roomClient.getName() + ": " + args[0];
        Room currentRoom = roomClient.getCurrentRoom();
        messageHistoryHolder.storeMessageFor(currentRoom, msg);
        currentRoom.getRoomClients().values().forEach(rc -> rc.write(msg));
    }
}
