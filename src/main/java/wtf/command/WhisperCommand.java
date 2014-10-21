package wtf.command;

import wtf.model.RoomClient;
import wtf.model.RoomCommand;

import java.util.Arrays;

public class WhisperCommand implements RoomCommand {

    @Override
    public void execute(RoomClient roomClient, String... args) {
        RoomClient recipient = roomClient.getCurrentRoom().getRoomClients().get(args[0]);
        String message = concat(Arrays.copyOfRange(args, 1, args.length));
        recipient.write("Private message from " + roomClient.getName() + ": " + message);
    }

    private static String concat(String[] parts) {
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
