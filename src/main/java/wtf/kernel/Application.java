package wtf.kernel;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import wtf.kernel.network.NetworkServer;
import wtf.kernel.network.impl.NetworkServerImpl;
import wtf.kernel.network.internal.NetworkServerInternal;

import javax.inject.Inject;

public class Application extends AbstractModule {

    private final ApplicationListener appListener;
    private final Injector injector;

    @Inject
    private TaskManager taskManager;
    @Inject
    private NetworkServer networkServer;

    public Application(ApplicationListener applicationListener) {
        Preconditions.checkNotNull(applicationListener);
        appListener = applicationListener;
        injector = Guice.createInjector(this, appListener);
    }

    public ListenableFuture<?> start(int port) {
        injector.injectMembers(this);
        return taskManager.submit(() -> start0(port));
    }

    private void start0(int port) {
        injector.injectMembers(appListener);
        appListener.onStart();
        ((NetworkServerInternal) networkServer).bind(port);
    }

    @Override
    protected void configure() {
        bind(EventBus.class).to(EventBusImpl.class).in(Scopes.SINGLETON);
        bind(TaskManager.class).to(TaskManagerImpl.class).in(Scopes.SINGLETON);
        bind(NetworkServer.class).to(NetworkServerImpl.class).in(Scopes.SINGLETON);
    }
}
