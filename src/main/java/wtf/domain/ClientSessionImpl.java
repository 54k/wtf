package wtf.domain;

import wtf.kernel.NetworkSession;

public class ClientSessionImpl implements ClientSession {

    private String name;
    private NetworkSession networkChannel;

    public ClientSessionImpl(String name, NetworkSession networkChannel) {
        this.name = name;
        this.networkChannel = networkChannel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void write(String msg) {
        networkChannel.write(msg);
    }

    @Override
    public void close() {
        networkChannel.close();
    }
}
