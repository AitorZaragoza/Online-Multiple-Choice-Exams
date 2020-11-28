package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void addStudent(ClientInterface client) throws RemoteException;
    void sendAnswer(ClientInterface client, Answer answer) throws RemoteException;

}
