package client;

import common.ClientInterface;
import common.Question;

import java.util.Scanner;

public class ClientImplementation implements ClientInterface {

    String studentId;


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

    }

    @Override
    public int sendGrade(ClientInterface student) {
        return 0;
    }
}
