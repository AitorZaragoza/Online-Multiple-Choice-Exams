package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    public String question;
    public List<String> choice = new ArrayList<>();

    public Question() { }


    public Question (String question, List<String> choice){
        this.question = question;
        this.choice = choice;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoice() {
        return choice;
    }

    public int getNumberChoice(){ return choice.size();}

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

}