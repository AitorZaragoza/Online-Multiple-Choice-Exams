package client;

import common.Answer;
import common.ClientInterface;
import common.Question;

import java.nio.Buffer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {
    String studentId;
    Integer questionNumber = 0;
    Integer choiceMax;
    Answer answer = new Answer();

    public ClientImplementation() throws RemoteException {
        super();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Welcome to the exam. Enter the StudentID");
        this.studentId = keyboard.nextLine();
    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    public void sendMessageError(String message) {
        System.out.println(message);
        System.exit(0);
    }

    public void notifyStartExam(Question q) {
        /*cleanBuffer();*/
        System.out.println("Exam starts");
        sendQuestion(q);
        synchronized (this) {
            this.notify();
        }
    }

    public void printChoices(List<String> list) {
        for (String string : list) {
            System.out.println(string);
        }
        System.out.println(" ");
    }

    public void sendQuestion(Question q) {
        answer.setQuestion(q.getQuestion());
        choiceMax = q.getNumberChoice();
        System.out.println(q.getQuestion());
        printChoices(q.getChoice());
        System.out.println("Enter answer");
        clearBuffer();
    }

    public void sendGrade(double grade) {
        System.out.println("The grade obtained is: " + grade);
        System.exit(0);
    }

    public void clearBuffer(){
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
    }

    public void writeAnswer() {
        Scanner keyboard = new Scanner(System.in);
        if (!keyboard.hasNextInt()) {
            keyboard.next();
            System.out.println("ERROR type: Please enter an answer that is between 1 and " + choiceMax);
            writeAnswer();
        } else {
            checkRangeAnswer(keyboard.nextInt(), 1, choiceMax);
        }
    }

    public void checkRangeAnswer(int number, int choiceMin, int choiceMax) {
        if (number >= choiceMin && number <= choiceMax) {
            answer.setAnswer(number);
            answer.setQuestionNumber(questionNumber);
            questionNumber++;
        } else {
            System.out.println("ERROR interval: Please enter an answer that is between " + choiceMin + " and " + choiceMax);
            writeAnswer();
        }
    }

}
