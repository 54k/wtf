package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.Room;
import wtf.domain.RoomClient;

public class LeaveRoomCommand implements Command {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room defaultRoom = roomClient.getCurrentRoom().getLobby().getDefaultRoom();
        roomClient.joinRoom(defaultRoom);
    }
}
