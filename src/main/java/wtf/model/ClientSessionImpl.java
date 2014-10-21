package wtf.model;

import wtf.net.NetworkChannel;

public class ClientSessionImpl implements ClientSession {

    private String name;
    private NetworkChannel networkChannel;

    public ClientSessionImpl(String name, NetworkChannel networkChannel) {
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
