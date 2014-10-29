package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.Room;
import wtf.domain.RoomClient;

public class RoomListCommand implements Command {

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
