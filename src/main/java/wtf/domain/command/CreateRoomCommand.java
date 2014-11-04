package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.Room;
import wtf.domain.RoomClient;

public class CreateRoomCommand implements Command {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room room = roomClient.getCurrentRoom().getLobby().createRoom(args[0]);
        roomClient.joinRoom(room);
    }
}
