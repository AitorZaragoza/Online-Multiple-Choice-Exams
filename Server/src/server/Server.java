package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Server {
    

    public static void readCsv() {

        String csvFile = "../ExamQuestions.csv";
        BufferedReader br = null;
        String line;
        List<String> questions = new ArrayList<String>();
        List<String> correct_answer = new ArrayList<String>();
        List<String> temporal_answer;
        List<List<String>> answers = new ArrayList<List<String>>();


        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                temporal_answer = new ArrayList<>();
                String[] data = line.split(";");

                questions.add(data[0]);
                correct_answer.add(data[data.length - 1]);

                for (int i = 1; i < data.length - 1; i++){
                    temporal_answer.add(data[i]);
                }
                answers.add(temporal_answer);

            }
            /*System.out.println(questions.get(1));
            System.out.println(answers.get(1));
            System.out.println(correct_answer.get(1));*/


        } catch (FileNotFoundException e) {
            e.printStackTrace();

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


    public static void main(String[] args){

        readCsv();



    }

}
