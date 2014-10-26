package wtf.service;

import wtf.util.Handler;

public interface NetworkServer {

    void bind(int port);

    void shutdown();

    void onConnection(Handler<NetworkSession> sessionHandler);
}