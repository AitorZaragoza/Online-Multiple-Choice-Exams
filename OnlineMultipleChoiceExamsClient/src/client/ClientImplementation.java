package client;

import common.ClientInterface;
import common.Question;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {

    String studentId;
    HashMap<String, Integer> questionAnswer = new HashMap<>();

    public ClientImplementation() throws RemoteException {
        super();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter studentID");
        this.studentId = keyboard.nextLine();
    }

    @Override
    public void sendQuestion(Question q) {
        System.out.println(q.getQuestion());
        System.out.println(q.getChoice());

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter answer");
        int answer = keyboard.nextInt();
        questionAnswer.put(q.getQuestion(), answer);

    }

    @Override
    public int sendGrade(ClientInterface student) {
        return 0;
    }

    public void sendMessage(String message){
        System.out.println(message);
    }

    public void notifyStartExam(){
        System.out.println("El examen comença");
        synchronized (this) {
            this.notify();
        }
    }

}
