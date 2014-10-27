package wtf.service;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

import javax.inject.Inject;

public final class Application extends AbstractModule {

    private final ApplicationListener appListener;
    private final Injector injector;

    @Inject
    private TaskManager taskManager;

    public Application(ApplicationListener applicationListener) {
        Preconditions.checkNotNull(applicationListener);
        appListener = applicationListener;
        injector = Guice.createInjector(this, appListener);
        injector.injectMembers(this);
    }

    public ListenableFuture<?> start() {
        return taskManager.submit(this::start0);
    }

    private void start0() {
        injector.injectMembers(appListener);
        appListener.onStart();
    }

    public ListenableFuture<?> shutdown() {
        return taskManager.submit(this::shutdown0);
    }

    private void shutdown0() {
        appListener.onShutdown();
    }

    @Override
    protected void configure() {
        bind(TaskManager.class).to(TaskManagerImpl.class).in(Scopes.SINGLETON);
        bind(NetworkServer.class).to(NetworkServerImpl.class).in(Scopes.SINGLETON);
    }
}
