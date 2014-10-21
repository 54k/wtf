package wtf.model;

import wtf.command.CreateRoomCommand;
import wtf.command.CurrentRoomCommand;
import wtf.command.EnterRoomCommand;
import wtf.command.LeaveRoomCommand;
import wtf.command.LogoutCommand;
import wtf.command.RoomListCommand;
import wtf.command.WhisperCommand;
import wtf.net.NetworkServer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Application implements Lobby, ApplicationListener {

    private Room defaultRoom;
    private Map<String, Room> roomsByName = new ConcurrentHashMap<>();
    private RoomMessageHandlerImpl roomCommandHandler;

    public Application() {
        roomCommandHandler = new RoomMessageHandlerImpl();
        roomCommandHandler.addRoomCommand("/whisper", new WhisperCommand());
        roomCommandHandler.addRoomCommand("/room", new CurrentRoomCommand());
        roomCommandHandler.addRoomCommand("/rooms", new RoomListCommand());
        roomCommandHandler.addRoomCommand("/create", new CreateRoomCommand());
        roomCommandHandler.addRoomCommand("/enter", new EnterRoomCommand());
        roomCommandHandler.addRoomCommand("/leave", new LeaveRoomCommand());
        roomCommandHandler.addRoomCommand("/logout", new LogoutCommand());

        defaultRoom = new Room(this, "main room");
        roomsByName.put(defaultRoom.getRoomName(), defaultRoom);
    }

    public void start() {
        NetworkServer networkServer = new NetworkServer();
        networkServer.onConnection(new Authenticator(this));
        networkServer.bind(8080);
    }

    @Override
    public Room getDefaultRoom() {
        return defaultRoom;
    }

    @Override
    public Room createRoom(String name) {
        if (roomsByName.containsKey(name)) {
            return getRoomByName(name);
        }
        Room room = new Room(this, name);
        roomsByName.put(room.getRoomName(), room);
        return room;
    }

    @Override
    public Room getRoomByName(String name) {
        return roomsByName.get(name);
    }

    @Override
    public Map<String, Room> getRooms() {
        return Collections.unmodifiableMap(roomsByName);
    }

    @Override
    public ClientSessionListener onLogIn(ClientSession clientSession) {
        RoomClientImpl roomClient = new RoomClientImpl(clientSession, roomCommandHandler);
        roomClient.enterRoom(defaultRoom);
        return roomClient;
    }
}
