package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public void sendQuestion (Question q);
    public int sendGrade(ClientInterface student);
}
