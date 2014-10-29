package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.RoomClient;

public class LogoutCommand implements Command {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        roomClient.close();
    }
}
