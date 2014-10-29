package wtf.kernel;

import wtf.util.TypedEventBus;

import javax.inject.Inject;

public class EventBusImpl implements EventBus {

    @Inject
    private TaskManager taskManager;
    private final TypedEventBus typedEventBus;

    public EventBusImpl() {
        typedEventBus = new TypedEventBus();
    }

    @Override
    public <T> T post(Class<T> type) {
        return typedEventBus.post(type);
    }

    @Override
    public void register(Object listener) {
        typedEventBus.register(listener);
    }

    @Override
    public <T> void register(Class<T> type, T listener) {
        typedEventBus.register(type, listener);
    }

    @Override
    public void unregister(Object listener) {
        typedEventBus.unregister(listener);
    }

    @Override
    public <T> void unregister(Class<T> type, T listener) {
        typedEventBus.unregister(type, listener);
    }
}
