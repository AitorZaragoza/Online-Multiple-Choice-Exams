package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ServerInterface extends Remote {
    public void addStudent(ClientInterface student) throws RemoteException;
    public void sendAnswer(HashMap<String, Integer> answer) throws RemoteException;

}

