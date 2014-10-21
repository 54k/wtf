package wtf.command;

import wtf.model.Room;
import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class EnterRoomCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room room = roomClient.getCurrentRoom().getLobby().getRoomByName(args[0]);
        roomClient.enterRoom(room);
    }
}
