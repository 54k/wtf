package wtf.kernel;

import wtf.util.Handler;

public interface NetworkServer {

    void onConnection(Handler<NetworkSession> sessionHandler);
}