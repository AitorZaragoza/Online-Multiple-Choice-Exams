package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void registerStudents(ClientInterface client, String universityID) throws RemoteException;
}
