package wtf.domain;

public interface LoginListener {
    ClientSessionListener onLogin(ClientSession clientSession);
}
