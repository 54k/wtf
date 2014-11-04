package wtf.domain;

import wtf.kernel.NetworkSession;

public class ClientSessionImpl implements ClientSession {

    private String name;
    private NetworkSession networkSession;

    public ClientSessionImpl(String name, NetworkSession networkSession) {
        this.name = name;
        this.networkSession = networkSession;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void write(String msg) {
        networkSession.write(msg);
    }

    @Override
    public void close() {
        networkSession.close();
    }
}
