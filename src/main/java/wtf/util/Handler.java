package wtf.util;

@FunctionalInterface
public interface Handler<T> {

    void handle(T event);
}
