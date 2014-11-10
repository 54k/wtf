package wtf.kernel.network;

import wtf.util.Handler;

public interface NetworkServer {

    void onConnection(Handler<NetworkSession> sessionHandler);
}