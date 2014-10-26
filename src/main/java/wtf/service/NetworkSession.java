package wtf.service;

import wtf.util.Handler;

public interface NetworkSession {

    void write(String msg);

    void close();

    void onMessage(Handler<String> messageHandler);

    void onClose(Handler<Void> closeHandler);
}
