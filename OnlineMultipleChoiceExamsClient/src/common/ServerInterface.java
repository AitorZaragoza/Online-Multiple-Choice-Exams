package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void addStudent(ClientInterface student);
    public void sendAnswer(Question question);

}
