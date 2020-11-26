package common;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private String question;
    private List<String> choice;

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

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

}
