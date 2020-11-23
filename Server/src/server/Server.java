package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Server {

    public static List<String> questions = new ArrayList<>();
    public static List<String> correct_answer = new ArrayList<>();
    public static List<List<String>> answers = new ArrayList<>();

    private static Registry startRegistry(Integer port) throws RemoteException {
        if(port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        }
        catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    public static void readCsv() {

        String csvFile = "ExamQuestions.csv";
        BufferedReader br = null;
        String line;

        List<String> temporal_answer;


        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {


                String[] data = line.split(";");
                int size = data.length;
                temporal_answer = new ArrayList<>(size - 2);

                questions.add(data[0]);
                correct_answer.add(data[data.length - 1]);

                temporal_answer.addAll(Arrays.asList(data).subList(1, size - 1));
                answers.add(temporal_answer);

            }
            System.out.println(questions.get(1));
            System.out.println(answers.get(1));
            System.out.println(correct_answer.get(1));


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


    public static void main(String[] args) {


        long startTime = System.currentTimeMillis();
        long endTime = startTime + 15000;
        readCsv();

        try {
            Registry registry = startRegistry(null);
            ServerImplementation obj = new ServerImplementation();
            registry.bind("Hello", obj);

            try {
                while (true) {
                    synchronized (obj) {

                        while (obj.getNumStudents() < 2) {  //Limite de tiempo o aforo  --- System.currentTimeMillis() <= endTime

                            System.out.println("Students in the class " + obj.getNumStudents());
                            obj.wait();

                        }
                        System.out.println("Starting Exam");
                        obj.notifyStartExam(questions, answers);
                        obj.wait();

                        System.out.println("Finishing game");
                    }
                }

            } catch (InterruptedException e) {
                System.err.println("Server exception: " + e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }


    }

}
