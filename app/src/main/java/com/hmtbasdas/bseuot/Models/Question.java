package com.hmtbasdas.bseuot.Models;

import java.io.Serializable;

public class Question implements Serializable {
    private final String questionID;
    private final String questionTITLE;
    private final String questionTEXT;
    private final String questionDATE;
    private final String questionUserID;
    private final Boolean questionSTATUS;

    public Question(String questionID, String questionTITLE, String questionTEXT, String questionDATE, String questionUserID, Boolean questionSTATUS) {
        this.questionID = questionID;
        this.questionTITLE = questionTITLE;
        this.questionTEXT = questionTEXT;
        this.questionDATE = questionDATE;
        this.questionUserID = questionUserID;
        this.questionSTATUS = questionSTATUS;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getQuestionTITLE() {
        return questionTITLE;
    }

    public String getQuestionTEXT() {
        return questionTEXT;
    }

    public String getQuestionDATE() {
        return questionDATE;
    }

    public String getQuestionUserID() {
        return questionUserID;
    }

    public Boolean getQuestionSTATUS() {
        return questionSTATUS;
    }
}
