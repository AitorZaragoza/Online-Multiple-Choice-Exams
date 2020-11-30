package server;


import common.ClientInterface;

import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

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
            String start_word = "end";
            ServerImplementation.Interrupt interrupt = new ServerImplementation.Interrupt(obj, start_word);

            while (true) {
                obj.readExamFile();
                obj.startExam();
                interrupt.start();
                synchronized (obj) {
                    while (obj.end == false) {
                        obj.wait();

                    }
                    System.out.println("S'ha acabat l'examen");
                    List<ClientInterface> error_students = new ArrayList<ClientInterface>();
                    for (ClientInterface c : obj.students) {
                        try {
                            c.sendGrade(obj.grades.get(c.getStudentId()));
                        } catch (RemoteException e) {
                            error_students.add(c);
                        }
                    }
                    for (ClientInterface c : error_students) {
                            obj.students.remove(c);
                    }

                    obj.writeGradesToCsvFile();
                    System.exit(0);
                }
            }
        }catch(Exception e){
                System.err.println("Server exception: " + e.toString());
                e.printStackTrace();
            }
        }

    }

