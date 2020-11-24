package common;

import java.rmi.RemoteException;

public interface OnlineMultipleChoiceExamsServer {
    void register(OnlineMultipleChoiceExamsClient client) throws RemoteException;
}
