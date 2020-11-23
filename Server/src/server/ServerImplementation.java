package server;

import common.ClientInterface;
import common.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {
    private List<ClientInterface> clients = new ArrayList<>();
    private ArrayList<String> ID = new ArrayList<>();
    int answers = 0;

    protected ServerImplementation() throws RemoteException {
        super();
    }

    @Override
    public void registerStudents(ClientInterface client, String universityID) {
        synchronized (this) {
            if(this.clients.size() < 4) {
                clients.add(client);
                this.ID.add(universityID);
                this.notify(); //??
            }
        }
    }

    public void notifyStartExam( List<String> questions, List<List<String>> answers){
        List<ClientInterface> error_clients = new ArrayList<>();
        for (ClientInterface client : this.clients) {
            try {
                client.notifyStartExam(questions, answers);
            } catch (RemoteException e) {
                System.out.println("Client is not reachable");
                error_clients.add(client);
            }
        }
        for(ClientInterface c: error_clients){
            this.clients.remove(c);
            this.ID.remove(c);
        }
    }

    public int getNumStudents(){
        return clients.size();
    }

}
