package wtf.domain;

public interface Command {

    void execute(RoomClient roomClient, String... args);
}
