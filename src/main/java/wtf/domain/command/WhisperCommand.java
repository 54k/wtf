package wtf.domain.command;

import wtf.domain.Command;
import wtf.domain.RoomClient;

import java.util.Arrays;

public class WhisperCommand implements Command {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        RoomClient recipient = roomClient.getCurrentRoom().getRoomClients().get(args[0]);
        String message = concatenateParts(Arrays.copyOfRange(args, 1, args.length));
        recipient.write("Private message from " + roomClient.getName() + ": " + message);
    }

    private static String concatenateParts(String[] parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                sb.append(parts[i]);
            } else {
                sb.append(" ").append(parts[i]);
            }
        }
        return sb.toString();
    }
}
