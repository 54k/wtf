package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.Room;
import wtf.domain.RoomClient;

public class EnterRoomCommand implements Command {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room room = roomClient.getCurrentRoom().getLobby().getRoomByName(args[0]);
        roomClient.joinRoom(room);
    }
}
