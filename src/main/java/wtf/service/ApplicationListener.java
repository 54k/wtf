package wtf.service;

import com.google.inject.Module;

public interface ApplicationListener extends Module {

    void onStart();

    void onShutdown();
}
