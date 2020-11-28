package client;

import common.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

public class Client {
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClientImplementation student = new ClientImplementation();
            ServerInterface stub = (ServerInterface) registry.lookup("Hello");
            synchronized(student) {
                student.setStudentId();
                stub.addStudent(student);
                student.wait();
                stub.sendAnswer(student.questionAnswer);

            }












        } catch (RemoteException e) {
            System.err.println("remote exception: " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("client exception: " + e.toString());
            e.printStackTrace();
        }
    }

}