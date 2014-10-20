package wtf.room;

import wtf.net.NetworkChannel;
import wtf.util.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Authenticator implements Handler<NetworkChannel> {

    private final Handler<RoomClient> roomClientHandler;
    private final Map<String, RoomClient> roomClientsByName = new ConcurrentHashMap<>();

    public Authenticator(Handler<RoomClient> roomClientHandler) {
        this.roomClientHandler = roomClientHandler;
    }

    @Override
    public void handle(NetworkChannel networkChannel) {
        networkChannel.write("Please enter your name");
        networkChannel.onMessage(name -> authenticate(networkChannel, name));
    }

    private void authenticate(NetworkChannel networkChannel, String name) {
        if (roomClientsByName.containsKey(name)) {
            networkChannel.write("Name already exists, try again");
        } else {
            RoomClient roomClient = new RoomClient(name, networkChannel);
            roomClientsByName.put(roomClient.getName(), roomClient);
            roomClientHandler.handle(roomClient);
        }
    }
}
