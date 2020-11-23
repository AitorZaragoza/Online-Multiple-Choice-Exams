package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientInterface extends Remote {

    void notifyStartExam(List<String> questions, List<List<String>> answers) throws RemoteException;
}
