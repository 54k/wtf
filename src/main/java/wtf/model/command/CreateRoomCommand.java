package wtf.model.command;

import wtf.model.Room;
import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class CreateRoomCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room room = roomClient.getCurrentRoom().getLobby().createRoom(args[0]);
        roomClient.enterRoom(room);
    }
}
