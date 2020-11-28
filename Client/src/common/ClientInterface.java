package common;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientInterface extends Remote {

    public void sendQuestion (Question q) throws RemoteException;
    public int sendGrade(ClientInterface student) throws RemoteException;
    public void sendMessage(String message) throws  RemoteException;
    public void notifyStartExam(Question q) throws RemoteException;
}
