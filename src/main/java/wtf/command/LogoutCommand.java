package wtf.command;

import wtf.model.RoomClient;
import wtf.model.RoomCommand;

public class LogoutCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        roomClient.close();
    }
}
