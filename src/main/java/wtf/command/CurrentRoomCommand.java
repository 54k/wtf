package wtf.command;

import wtf.model.Room;
import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class CurrentRoomCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room currentRoom = roomClient.getCurrentRoom();
        roomClient.write("room: " + currentRoom.getRoomName() + ", users: " + currentRoom.getRoomClients().size());
    }
}
