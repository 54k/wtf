package wtf.domain;

import wtf.util.Listener;

@Listener
public interface RoomEventListener {

    void onRoomClientRegistered(RoomClient roomClient, Room room);

    void onRoomClientUnregistered(RoomClient roomClient, Room room);
}
