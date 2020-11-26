package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public void notifyStartExam() throws RemoteException;
    public void notifyMark() throws RemoteException;
}
