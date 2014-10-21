package wtf.command;

import wtf.model.Room;
import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class RoomListCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        Room currentRoom = roomClient.getCurrentRoom();
        currentRoom.getLobby().getRooms().forEach((rn, r) -> {
            String msg = "room: " + rn + ", users: " + r.getRoomClients().size();
            if (r == currentRoom) {
                msg += " <- you are here";
            }
            roomClient.write(msg);
        });
    }
}
