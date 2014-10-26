package wtf.model;

import com.google.inject.AbstractModule;
import wtf.command.CreateRoomCommand;
import wtf.command.CurrentRoomCommand;
import wtf.command.EnterRoomCommand;
import wtf.command.LeaveRoomCommand;
import wtf.command.LogoutCommand;
import wtf.command.RoomListCommand;
import wtf.command.WhisperCommand;
import wtf.service.ApplicationListener;
import wtf.service.NetworkServer;
import wtf.service.NetworkServerImpl;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatApplication extends AbstractModule implements Lobby, ApplicationListener {

    private Room defaultRoom;
    private Map<String, Room> roomsByName = new ConcurrentHashMap<>();
    private RoomMessageHandlerImpl roomCommandHandler;
    @Inject
    private NetworkServer networkServer;

    public ChatApplication() {
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
    protected void configure() {

    }

    @Override
    public void onStart() {
        networkServer.onConnection(new Authenticator(this));
        networkServer.bind(8080);
    }

    @Override
    public void onShutdown() {
        networkServer.shutdown();
    }

    @Override
    public ClientSessionListener onLogin(ClientSession clientSession) {
        RoomClientImpl roomClient = new RoomClientImpl(clientSession, roomCommandHandler);
        roomClient.enterRoom(defaultRoom);
        return roomClient;
    }
}
