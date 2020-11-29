package server;

import common.Answer;
import common.ClientInterface;
import common.Question;
import common.ServerInterface;

import java.io.BufferedReader;
import java.io.FileReader;
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
    public boolean end = false;


    public ServerImplementation() throws RemoteException {
        super();
    }

    public void startExam() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Prem tecla per tancar l'examen");
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
        String csvFile = "ExamQuestions.csv";
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
            } else {
                student.sendMessage("Has arribat tard al examen");
                System.exit(0);
            }
        }
    }

    @Override
    public void sendAnswer(ClientInterface student, Answer answer) throws RemoteException {
            synchronized (this) {
                studentIsInGrades(student);
                checkAnswer(student, answer);
                this.notify();
            }
                if(exam.size() > answer.getQuestionNumber() + 1){
                    student.notifyQuestion(exam.get(answer.getQuestionNumber()+1));
                }else{
                    student.notifyGrade(grades.get(student.getStudentId()));
                }
    }

    public void checkAnswer(ClientInterface student, Answer answer) throws RemoteException {
        if (examSolution.get(answer.getQuestion()).compareTo(answer.getAnswer()) == 0) {
            double answersCorrects = calculateAnswersCorrects(student) + 1.0;
            double grade = calculateGrade( answersCorrects, exam.size());
            grades.put(student.getStudentId(), grade);
        }
    }

    public void studentIsInGrades(ClientInterface student) throws RemoteException {
            if(!grades.containsKey(student.getStudentId())){
                grades.put(student.getStudentId(), 0.0);
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
}
