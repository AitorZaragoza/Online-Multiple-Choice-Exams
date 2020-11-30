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

    public void showQuestion(Question q) {
        System.out.println(q.getQuestion());
        System.out.println(q.getChoice());
        System.out.println("Enter answer");
        answer.setQuestion(q.getQuestion());
    }

    public void writeAnswer() {
        Scanner keyboard = new Scanner(System.in);
        answer.setAnswer(keyboard.nextInt());
        answer.setQuestionNumber(questionNumber);
        questionNumber++;
    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    public void notifyStartExam(Question q) {
        System.out.println("El examen comença");
        showQuestion(q);
        synchronized (this) {
            this.notify();
        }
    }

    public void sendQuestion(Question q){
            showQuestion(q);
        }

    public void sendGrade(double grade){
        System.out.println("La nota obtinguda és: " + grade);
        System.exit(0);
    }
}




