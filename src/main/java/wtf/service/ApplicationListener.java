package wtf.service;

import com.google.inject.Module;
import wtf.model.ClientSession;
import wtf.model.ClientSessionListener;

public interface ApplicationListener extends Module {

    void onStart();

    void onShutdown();

    ClientSessionListener onLogin(ClientSession clientSession);
}
