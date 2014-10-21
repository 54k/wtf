package wtf.model;

public interface ClientSession {

    String getName();

    void write(String msg);

    void close();
}
