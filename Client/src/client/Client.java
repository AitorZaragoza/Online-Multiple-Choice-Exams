package client;

import common.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClientImplementation student = new ClientImplementation();
            ServerInterface stub = (ServerInterface) registry.lookup("Hello");

            stub.addStudent(student);

            synchronized(student) {
                student.setStudentId();
                student.wait();
                stub.sendAnswer(student, student.answer);
                student.wait();


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
