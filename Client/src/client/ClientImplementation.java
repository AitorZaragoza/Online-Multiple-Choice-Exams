package client;

import common.Answer;
import common.ClientInterface;
import common.Question;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {

    String studentId;
    Integer questionNumber = 0;
    Answer answer = new Answer();


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

        answer.setQuestion(q.getQuestion());
        answer.setAnswer(keyboard.nextInt());
        answer.setQuestionNumber(questionNumber);
        questionNumber++;

    }

    @Override
    public int sendGrade(ClientInterface student) {
        return 0;
    }

    public void sendMessage(String message){
        System.out.println(message);
    }

    public void notifyStartExam(Question q){
        System.out.println("El examen comença");
        sendQuestion(q);
        synchronized (this) {
            this.notify();
        }

    }

}