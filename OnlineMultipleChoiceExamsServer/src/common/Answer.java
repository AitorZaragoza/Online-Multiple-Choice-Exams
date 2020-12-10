package common;


import java.io.Serializable;

public class Answer implements Serializable {
    String question;
    Integer answer;
    Integer questionNumber;

    public Answer() {
    }

    public Answer(String question, Integer answer, Integer questionNumber) {
        this.question = question;
        this.answer = answer;
        this.questionNumber = questionNumber;

    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }


}