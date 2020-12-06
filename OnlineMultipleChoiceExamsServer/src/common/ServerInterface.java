package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void addStudent(ClientInterface student) throws RemoteException;
    public void sendAnswer(ClientInterface student, Answer answer) throws RemoteException;

}

