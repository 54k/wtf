package wtf.command;

import wtf.model.MessageHistoryHolder;
import wtf.model.Room;
import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class BroadcastCommand implements RoomCommand {

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
