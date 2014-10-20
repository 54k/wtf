package wtf;

import wtf.model.Authenticator;
import wtf.model.Lobby;
import wtf.model.RoomCommandHandler;
import wtf.model.command.CreateRoomCommand;
import wtf.model.command.CurrentRoomCommand;
import wtf.model.command.EnterRoomCommand;
import wtf.model.command.LeaveRoomCommand;
import wtf.model.command.LogoutCommand;
import wtf.model.command.RoomListCommand;
import wtf.model.command.WhisperCommand;
import wtf.net.NetworkServer;

public class App {

    public static void main(String[] args) {
        RoomCommandHandler roomCommandHandler = new RoomCommandHandler();
        roomCommandHandler.addRoomCommand("/whisper", new WhisperCommand());
        roomCommandHandler.addRoomCommand("/room", new CurrentRoomCommand());
        roomCommandHandler.addRoomCommand("/rooms", new RoomListCommand());
        roomCommandHandler.addRoomCommand("/create", new CreateRoomCommand());
        roomCommandHandler.addRoomCommand("/enter", new EnterRoomCommand());
        roomCommandHandler.addRoomCommand("/leave", new LeaveRoomCommand());
        roomCommandHandler.addRoomCommand("/logout", new LogoutCommand());

        Lobby lobby = new Lobby();

        NetworkServer networkServer = new NetworkServer();
        networkServer.onConnection(new Authenticator(lobby, roomCommandHandler));
        networkServer.bind(8080);
    }
}
