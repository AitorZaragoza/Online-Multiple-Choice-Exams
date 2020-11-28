package common;

import java.io.Serializable;
import java.util.HashMap;

public class Answer implements Serializable {

    HashMap<String, Integer> questionAnswer = new HashMap<>();
    Integer questionNumber;

    public Answer(){

    }

    public Answer(HashMap<String, Integer> questionAnswer, Integer questionNumber ){
        this.questionAnswer = questionAnswer;
        this.questionNumber = questionNumber;

    }

    public void setQuestionAnswer(String question, Integer answer) {
        this.questionAnswer.put(question, answer);
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public HashMap<String, Integer> getQuestionAnswer() {
        return questionAnswer;
    }
}
