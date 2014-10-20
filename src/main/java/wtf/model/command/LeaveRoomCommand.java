package wtf.model.command;

import wtf.model.Room;
import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class LeaveRoomCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room defaultRoom = roomClient.getCurrentRoom().getLobby().getDefaultRoom();
        roomClient.enterRoom(defaultRoom);
    }
}
