package wtf.domain;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import wtf.domain.command.CreateRoomCommand;
import wtf.domain.command.CurrentRoomCommand;
import wtf.domain.command.EnterRoomCommand;
import wtf.domain.command.LeaveRoomCommand;
import wtf.domain.command.LogoutCommand;
import wtf.domain.command.RoomListCommand;
import wtf.domain.command.WhisperCommand;
import wtf.kernel.ApplicationListener;
import wtf.kernel.EventBus;
import wtf.kernel.network.NetworkServer;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatApplication extends AbstractModule implements Lobby, ApplicationListener, LoginListener {

    @Inject
    private NetworkServer networkServer;
    @Inject
    private EventBus eventBus;
    @Inject
    private Injector injector;

    private Room defaultRoom;
    private Map<String, Room> roomsByName = new ConcurrentHashMap<>();
    private CommandHandlerImpl commandHandler;

    public ChatApplication() {
        commandHandler = new CommandHandlerImpl();
        commandHandler.addCommand("/whisper", new WhisperCommand());
        commandHandler.addCommand("/room", new CurrentRoomCommand());
        commandHandler.addCommand("/rooms", new RoomListCommand());
        commandHandler.addCommand("/create", new CreateRoomCommand());
        commandHandler.addCommand("/enter", new EnterRoomCommand());
        commandHandler.addCommand("/leave", new LeaveRoomCommand());
        commandHandler.addCommand("/logout", new LogoutCommand());
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
        injector.injectMembers(room);
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
        // NO OP
    }

    @Override
    public void onStart() {
        networkServer.onConnection(new Authenticator(this));
        eventBus.register(commandHandler);
        defaultRoom = createRoom("main room");
    }

    @Override
    public ClientSessionListener onLogin(ClientSession clientSession) {
        RoomClientImpl roomClient = new RoomClientImpl(clientSession, commandHandler);
        roomClient.joinRoom(defaultRoom);
        return roomClient;
    }
}
