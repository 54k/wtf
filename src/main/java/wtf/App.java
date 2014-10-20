package wtf;

import wtf.net.NetworkServer;
import wtf.room.Authenticator;
import wtf.room.Room;
import wtf.room.RoomDispatcher;

public class App {

    public static void main(String[] args) {
        RoomDispatcher roomDispatcher = new RoomDispatcher();
        roomDispatcher.addRoomCommandHandler("/room", (roomClient, message) -> roomClient.write("You are in " + roomClient.getCurrentRoom().getRoomName()));
        roomDispatcher.addRoomCommandHandler("/create", (roomClient, message) -> {
            Room room = roomClient.getCurrentRoom().getRoomDispatcher().createRoom(message[0]);
            roomClient.enterRoom(room);
        });
        roomDispatcher.addRoomCommandHandler("/enter", (roomClient, message) -> {
            Room room = roomClient.getCurrentRoom().getRoomDispatcher().getRoomByName(message[0]);
            roomClient.enterRoom(room);
        });
        roomDispatcher.addRoomCommandHandler("/leave", (roomClient, message) -> {
            Room defaultRoom = roomClient.getCurrentRoom().getRoomDispatcher().getDefaultRoom();
            roomClient.enterRoom(defaultRoom);
        });
        roomDispatcher.addRoomCommandHandler("/logout", (roomClient, message) -> roomClient.close());

        NetworkServer networkServer = new NetworkServer();
        networkServer.onConnection(new Authenticator(roomDispatcher));
        networkServer.bind(8080);
    }
}
