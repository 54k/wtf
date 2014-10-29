package wtf.domain;

public interface ClientSession {

    String getName();

    void write(String msg);

    void close();
}
