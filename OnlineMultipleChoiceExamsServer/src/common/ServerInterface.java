package common;

import java.rmi.RemoteException;

public interface ServerInterface {
    public void registerStudent(ClientInterface student) throws RemoteException;
    public void sendAnswer (ClientInterface student, int number) throws RemoteException;
}
