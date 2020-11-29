package common;

import java.rmi.RemoteException;

public interface ClientInterface {
    public void sendMessage(String message) throws RemoteException;
    public String getStudentId() throws  RemoteException;
    public void notifyStartExam(Question q) throws RemoteException;
    public void notifyQuestion(Question q) throws RemoteException;
    public void notifyGrade(double grade) throws RemoteException;;
}
