package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OnlineMultipleChoiceExamsClient extends Remote {
    public void notifyHello() throws RemoteException;
}
