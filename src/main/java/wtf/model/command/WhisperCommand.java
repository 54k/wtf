package wtf.model.command;

import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class WhisperCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        RoomClient recipient = roomClient.getCurrentRoom().getRoomClients().get(args[0]);
        recipient.write("Private message from " + roomClient.getName() + ": " + args[1]);
    }
}
