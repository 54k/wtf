package wtf.model;

public interface RoomCommand {

    void execute(RoomClient roomClient, String... args);
}
