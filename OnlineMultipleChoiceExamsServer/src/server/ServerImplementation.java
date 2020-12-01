package server;

import common.Answer;
import common.ClientInterface;
import common.Question;
import common.ServerInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    public List<ClientInterface> students = new ArrayList<>();
    public List<Question> exam = new ArrayList<>();
    public HashMap<String, Integer> examSolution = new HashMap<>();
    public HashMap<String, Double> grades = new HashMap<>();
    public boolean start = false;
    public static boolean end = false;


    public ServerImplementation() throws RemoteException {
        super();
    }

    public void startExam() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Prem qualsevol tecla per començar l'examen");
        keyboard.nextLine();
        synchronized (this) {
            this.start = true;
        }
        List<ClientInterface> error_students = new ArrayList<ClientInterface>();
        for (ClientInterface c : students) {
            try {
                c.notifyStartExam(exam.get(0)); // notificar als alumnes que comença l'examen i enviar la primera pregunta
            } catch (RemoteException e) {
                System.out.println(" Student is not reachable");
                error_students.add(c);
            }
        }
        for (ClientInterface c : error_students) {
            this.students.remove(c);
        }
    }

    public void readExamFile() {
        String csvFile = "exam.csv";
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                Question question = new Question();
                String[] data = line.split(";");
                int size = data.length;
                String questionRead = data[0];
                int answerRead = Integer.parseInt(data[data.length - 1]);
                List<String> choiceRead = new ArrayList<>(size - 2);

                choiceRead.addAll(Arrays.asList(data).subList(1, size - 1));
                question.setQuestion(questionRead);
                question.setChoice(choiceRead);
                examSolution.put(questionRead, answerRead);
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

    @Override
    public void addStudent(ClientInterface student) throws RemoteException {
        synchronized (this) {
            if (start == false) {
                students.add(student);
                grades.put(student.getStudentId(), 0.0);
            } else {
                student.sendMessage("Has arribat tard al examen");
                System.exit(0);
            }
        }
    }

    @Override
    public void sendAnswer(ClientInterface student, Answer answer) throws RemoteException {
        synchronized (this) {
            checkAnswer(student, answer);
            this.notify();
            }
            if(exam.size() > answer.getQuestionNumber() + 1){
                student.sendQuestion(exam.get(answer.getQuestionNumber()+1));
            }else{
                student.sendGrade(grades.get(student.getStudentId()));
            }
    }

    public void checkAnswer(ClientInterface student, Answer answer) throws RemoteException {
        if (examSolution.get(answer.getQuestion()).compareTo(answer.getAnswer()) == 0) {
            double answersCorrects = calculateAnswersCorrects(student) + 1.0;
            double grade = calculateGrade( answersCorrects, exam.size());
            grades.put(student.getStudentId(), grade);
        }
    }



    public double calculateGrade(double answersCorrects, int numQuestions){
        return (answersCorrects/numQuestions) * 10;
    }

    public double calculateAnswersCorrects(ClientInterface student) throws RemoteException {
        double actualGrade = grades.get(student.getStudentId());
        double relationGradeQuestion = 10.0/exam.size();
        return actualGrade/relationGradeQuestion;
    }

    public void writeGradesToCsvFile(){
        String fileName = "grades.csv";

        try (FileWriter writer = new FileWriter(fileName)){
            for (String idStudent: this.grades.keySet()){
                String key = idStudent;
                String value = this.grades.get(idStudent).toString();
                System.out.println(key + ": " + value);
                writer.append(key);
                writer.append(";");
                writer.append(value);
                writer.append(System.lineSeparator());
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endExam(){
        System.out.println("S'ha acabat l'examen");
        List<ClientInterface> error_students = new ArrayList<ClientInterface>();
        for (ClientInterface c : this.students) {
            try {
                c.sendGrade(this.grades.get(c.getStudentId()));
            } catch (RemoteException e) {
                error_students.add(c);
            }
        }
        for (ClientInterface c : error_students) {
            this.students.remove(c);
        }

        writeGradesToCsvFile();
        System.exit(0);
    }


    public static class Interrupt extends Thread {
        String interrupt_key = null;
        Object semaphore = null;

        //semaphore must be the syncronized object
        Interrupt(Object semaphore, String interrupt_key){
            this.semaphore = semaphore;
            this.interrupt_key = interrupt_key;
        }

        public void run(){
            System.out.println("Introdueix la paraula '" + interrupt_key + "' per acabar l'examen");
            while (true) {
                //read the key
                Scanner scanner = new Scanner(System.in);
                String x = scanner.nextLine();
                if (x.equals(this.interrupt_key)) {
                    //if is the key we expect, change the variable, notify and return(finish thread)
                    synchronized (this.semaphore) {
                        end = true;
                        this.semaphore.notify();
                        return;
                    }
                }
            }
        }



        }
    }




