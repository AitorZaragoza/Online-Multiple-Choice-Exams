package server;

import common.Answer;
import common.ClientInterface;
import common.Question;
import common.ServerInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {
    public String csvFile;
    public boolean start = false;
    public static boolean end = false;
    public List<ClientInterface> students = new ArrayList<>();
    public List<Question> exam = new ArrayList<>();
    public HashMap<String, Integer> examSolution = new HashMap<>();
    public HashMap<String, Double> grades = new HashMap<>();

    public ServerImplementation() throws RemoteException {
        super();
    }

    public String readCsvFileName() {
        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine() + ".csv";
    }

    public void checkExistCsvFile() {
        System.out.println("Enter the name of the exam file. NOTICE: Type the file name without the file extension .csv");
        csvFile = readCsvFileName();

        File archivo = new File(csvFile);
        if (!archivo.exists()) {
            System.out.println("There is no exam file with this name.");
            checkExistCsvFile();
        }
    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public boolean checkFormatCsv(String[] data) {
        if (data.length < 4) {
            return false;
        } else if (!isNumeric(data[data.length - 1])) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkRangeAnswerSolution(List<String> choiceRead, Integer answerRead) {
        if (answerRead > choiceRead.size()) {
            return false;
        } else {
            return true;
        }
    }

    public void readExamFile() {
        BufferedReader br = null;
        String line;
        checkExistCsvFile();
        examSolution.clear();
        exam.clear();


        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                Question question = new Question();
                String[] data = line.split(";");
                if (checkFormatCsv(data)) {
                    int size = data.length;
                    String questionRead = data[0];
                    int answerRead = Integer.parseInt(data[data.length - 1]);
                    List<String> choiceRead = new ArrayList<>(size - 2);
                    choiceRead.addAll(Arrays.asList(data).subList(1, size - 1));

                    if (checkRangeAnswerSolution(choiceRead, answerRead)) {
                        question.setQuestion(questionRead);
                        question.setChoice(choiceRead);
                        examSolution.put(questionRead, answerRead);
                        exam.add(question);
                    } else {
                        System.out.println("The format of " + csvFile + " is not correct, a solution to the question is out of the range of choices. Please fix the file or enter a different one");
                        readExamFile();
                        return;
                    }

                } else {
                    System.out.println("The format of " + csvFile + " is not correct. Please fix the file or enter a different one");
                    readExamFile();
                    return;
                }
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

    public void startExam() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Press any key for start the exam");
        keyboard.nextLine();
        synchronized (this) {
            this.start = true;
        }
        System.out.println("There are " + students.size() + " student/s on the exam ");
        List<ClientInterface> error_students = new ArrayList<ClientInterface>();
        for (ClientInterface c : students) {
            try {
                c.notifyStartExam(exam.get(0));
            } catch (RemoteException e) {
                System.out.println(" Student is not reachable");
                error_students.add(c);
            }
        }
        for (ClientInterface c : error_students) {
            this.students.remove(c);
        }
    }

    @Override
    public void addStudent(ClientInterface student) throws RemoteException {
        synchronized (this) {
            if (start == false) {
                if (grades.containsKey(student.getStudentId())) {
                    student.sendMessageError("Duplicate studentID. You can't take the exam");
                }
                students.add(student);
                grades.put(student.getStudentId(), 0.0);
                student.sendMessage("You have successfully registered for the exam. Please wait for the exam to start.");
            } else {
                student.sendMessageError("You were late for the exam");
            }
        }
    }

    @Override
    public void sendAnswer(ClientInterface student, Answer answer) throws RemoteException {
        synchronized (this) {
            checkAnswer(student, answer);
            this.notify();
        }
        if (exam.size() > answer.getQuestionNumber() + 1) {
            student.sendQuestion(exam.get(answer.getQuestionNumber() + 1));
        } else {
            double roundGrade = roundGrade(grades.get(student.getStudentId()), 2);
            student.sendGrade(roundGrade);
        }
    }

    public double calculateGrade(double answersCorrects, int numQuestions) {
        return (answersCorrects / numQuestions) * 10;
    }

    public double calculateAnswersCorrects(ClientInterface student) throws RemoteException {
        double actualGrade = grades.get(student.getStudentId());
        double relationGradeQuestion = 10.0 / exam.size();
        return actualGrade / relationGradeQuestion;
    }

    public double roundGrade(double grade, int decimalsNumber) {
        double partWhole, result;
        result = grade;
        partWhole = Math.floor(result);
        result = (result - partWhole) * Math.pow(10, decimalsNumber);
        result = Math.round(result);
        result = (result / Math.pow(10, decimalsNumber)) + partWhole;
        result = (double)Math.round(result * 100d) / 100d;
        return result;
    }

    public void checkAnswer(ClientInterface student, Answer answer) throws RemoteException {
        if (examSolution.get(answer.getQuestion()).compareTo(answer.getAnswer()) == 0) {
            double answersCorrects = calculateAnswersCorrects(student) + 1.0;
            double grade = calculateGrade(answersCorrects, exam.size());
            grades.put(student.getStudentId(), grade);
        }
    }

    public void writeGradesToCsvFile() {
        System.out.println("Please enter the name of the grades file. NOTICE: Type the file name without the file extension .csv");
        csvFile = readCsvFileName();

        try (FileWriter writer = new FileWriter(csvFile)) {
            System.out.println("\nGrades");
            for (String idStudent : this.grades.keySet()) {
                this.grades.put(idStudent, roundGrade(this.grades.get(idStudent), 2));
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

    public void endExam() {
        System.out.println("Exam is over");
        List<ClientInterface> error_students = new ArrayList<ClientInterface>();
        for (ClientInterface c : this.students) {
            try {
                double roundGrade = roundGrade(this.grades.get(c.getStudentId()), 2);
                c.sendGrade(roundGrade);
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

        Interrupt(Object semaphore, String interrupt_key) {
            this.semaphore = semaphore;
            this.interrupt_key = interrupt_key;
        }

        public void run() {
            System.out.println("Type the word '" + interrupt_key + "' to end the exam");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String x = scanner.nextLine();
                if (x.equals(this.interrupt_key)) {

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
