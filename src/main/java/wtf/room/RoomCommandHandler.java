package wtf.room;

@FunctionalInterface
public interface RoomCommandHandler {

    void handle(RoomClient roomClient, String... args);
}
