package server;

import common.ClientInterface;
import common.Question;
import common.ServerInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {
    public List<ClientInterface> students = new ArrayList<>();
    public List<Question> exam = new ArrayList<>();
    public HashMap<String, Integer> solutions = new HashMap<>();


    protected ServerImplementation() throws RemoteException {
        super();
    }

    public void readExamFile() {

        Question question = new Question();

        String csvFile = "ExamQuestions.csv";
        BufferedReader br = null;
        String line;


        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                String[] data = line.split(";");
                int size = data.length;

                String questionRead = data[0];
                int answerRead = Integer.parseInt(data[data.length - 1]);
                List<String> choiceRead = new ArrayList<>(size - 2);

                choiceRead.addAll(Arrays.asList(data).subList(1, size - 1));

                question.setQuestion(questionRead);
                question.setChoice(choiceRead);
                solutions.put(questionRead, answerRead);

                exam.add(question);

            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}