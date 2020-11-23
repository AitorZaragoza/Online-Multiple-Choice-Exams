package client;

import common.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {

    public ClientImplementation() throws RemoteException {
        super();
    }


    @Override
    public void notifyStartExam(List<String> questions, List<List<String>> answers) {
        System.out.println("The exam is going to start");
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("enter an integer");
//        secretNumber = keyboard.nextInt();


        synchronized (this) {

            for (int i = 0; i < questions.size(); i++) {
                int numberQuestion = i + 1;

                System.out.println(numberQuestion + "." + questions.get(i));
                System.out.println(answers.get(i));

                this.notify();
            }
        }
    }


}
