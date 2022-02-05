package com.hmtbasdas.bseuot.Listeners;

import com.hmtbasdas.bseuot.Models.Question;

public interface QuestionListener {
    void onQuestionClicked(Question question);
    void onReportQuestionClicked(Question question);
}
