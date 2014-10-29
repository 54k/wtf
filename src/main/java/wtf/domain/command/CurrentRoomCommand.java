package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.Room;
import wtf.domain.RoomClient;

public class CurrentRoomCommand implements Command {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room currentRoom = roomClient.getCurrentRoom();
        roomClient.write("room: " + currentRoom.getRoomName() + ", users: " + currentRoom.getRoomClients().size());
    }
}
