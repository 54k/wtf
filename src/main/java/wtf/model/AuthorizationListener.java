package wtf.model;

public interface AuthorizationListener {

    ClientSessionListener onLogin(ClientSession clientSession);
}
