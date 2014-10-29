package wtf.kernel;

public interface EventBus {

    public <T> T post(Class<T> type);

    public void register(Object listener);

    public <T> void register(Class<T> type, T listener);

    public void unregister(Object listener);

    public <T> void unregister(Class<T> type, T listener);
}
