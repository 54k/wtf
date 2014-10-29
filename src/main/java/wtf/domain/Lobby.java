package wtf.domain;

import java.util.Map;

public interface Lobby {

    Room getDefaultRoom();

    Room createRoom(String name);

    Room getRoomByName(String name);

    Map<String, Room> getRooms();
}
