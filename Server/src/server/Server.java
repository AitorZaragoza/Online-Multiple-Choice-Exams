package server;


import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static Registry startRegistry(Integer port)
            throws RemoteException {
        if (port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list();
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    public static void main(String args[]) {
        try {

            Registry registry = startRegistry(null);
            ServerImplementation obj = new ServerImplementation();
            registry.rebind("Exam", (ServerImplementation) obj);

            while (true) {
                obj.readExamFile();
                obj.startExam();

                synchronized (obj) {
                    while (obj.end == false) {
                        obj.wait();

                    }
                }
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}