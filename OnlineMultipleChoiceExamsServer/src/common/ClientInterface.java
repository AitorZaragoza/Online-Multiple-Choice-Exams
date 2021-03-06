package common;

import java.rmi.RemoteException;

public interface ClientInterface {
    public void sendMessage(String message) throws RemoteException;

    public void sendMessageError(String message) throws RemoteException;

    public String getStudentId() throws RemoteException;

    public void notifyStartExam(Question q) throws RemoteException;

    public void sendQuestion(Question q) throws RemoteException;

    public void sendGrade(double grade) throws RemoteException;

    ;
}
