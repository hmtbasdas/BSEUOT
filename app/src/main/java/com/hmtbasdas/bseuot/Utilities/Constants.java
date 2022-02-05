package com.hmtbasdas.bseuot.Utilities;

import android.app.Activity;

import com.hmtbasdas.bseuot.Activities.ConfessionActivity;
import com.hmtbasdas.bseuot.Activities.ContactActivity;
import com.hmtbasdas.bseuot.Activities.MealListActivity;
import com.hmtbasdas.bseuot.Activities.QuestionsActivity;
import com.hmtbasdas.bseuot.Activities.SchoolClubsActivity;
import com.hmtbasdas.bseuot.Activities.SchoolDormitoryActivity;

public class Constants {

    public static final String KEY_INTENT_COLLECTION = "intent_KEY";
    public static final String KEY_INTENT_ID = "intent_ID";

    public static final Activity[] KEY_ACTIVITIES = {new QuestionsActivity(), new SchoolClubsActivity(), new SchoolDormitoryActivity(), new ConfessionActivity(), new MealListActivity(), new ContactActivity(), new ContactActivity(), new ContactActivity()};
    public static final String KEY_PREFERENCE_NAME = "bseuotAppPreference";
    public static final String KEY_STUDENT_COLLECTIONS = "students";
    public static final String KEY_STUDENT_SECRET_COLLECTIONS = "secretValueS";
    public static final String KEY_STUDENT_MAIL = "studentMAIL";
    public static final String KEY_STUDENT_PASS = "studentPASSWORD";
    public static final String KEY_STUDENT_ID = "studentID";
    public static final String KEY_STUDENT_NAME = "studentNAME";
    public static final String KEY_STUDENT_SURNAME = "studentSURNAME";
    public static final String KEY_STUDENT_NO = "studentNO";
    public static final String KEY_STUDENT_IMAGE = "studentIMAGE";
    public static final String KEY_STUDENT_DEPARTMENT = "studentDEPARTMENT";
    public static final String KEY_STUDENT_DEPARTMENT_LINK = "studentDepartmentLINK";
    public static final String KEY_STUDENT_DEPARTMENT_LAST_VALUE = "studentDepartmentLastVALUE";

    public static final String KEY_UNI_PRIVATE_STATUS = "uniPrivateStatus";


    public static final String KEY_NEWS_COLLECTIONS = "news";
    public static final String KEY_NEWS_ID = "newsID";
    public static final String KEY_NEWS_EYE = "newsEYE";
    public static final String KEY_NEWS_TITLE = "newsTITLE";
    public static final String KEY_NEWS_DESC = "newsDESC";
    public static final String KEY_NEWS_DATE = "newsDATE";
    public static final String KEY_NEWS_IMAGE = "newsIMAGE";
    public static final String KEY_NEWS_STATUS = "newsSTATUS";

    public static final String KEY_COMMENT_COLLECTIONS = "comments";
    public static final String KEY_COMMENT_ID = "commentID";
    public static final String KEY_COMMENT_TYPE = "commentTYPE";
    public static final String KEY_COMMENT_TEXT = "commentTEXT";
    public static final String KEY_COMMENT_UserID = "commentUserID";
    public static final String KEY_COMMENT_UserNAME = "commentUserNAME";
    public static final String KEY_COMMENT_UserIMAGE = "commentUserIMAGE";
    public static final String KEY_COMMENT_DATE = "commentDATE";
    public static final String KEY_COMMENT_ObjectID = "commentObjectID";
    public static final String KEY_COMMENT_STATUS = "commentSTATUS";

    public static final String KEY_QUESTION_COLLECTIONS = "questions";
    public static final String KEY_QUESTION_ID = "questionID";
    public static final String KEY_QUESTION_TITLE = "questionTITLE";
    public static final String KEY_QUESTION_TEXT = "questionTEXT";
    public static final String KEY_QUESTION_DATE = "questionDATE";
    public static final String KEY_QUESTION_UserID = "questionUserID";
    public static final String KEY_QUESTION_UserNAME = "questionUserNAME";
    public static final String KEY_QUESTION_UserSURNAME = "questionUserSURNAME";
    public static final String KEY_QUESTION_STATUS = "questionSTATUS";

    public static final String KEY_REPORT_COMMENT_COLLECTIONS = "reportComments";
    public static final String KEY_REPORT_COMMENT_ID = "reportCommentID";
    public static final String KEY_REPORT_COMMENT_TYPE = "reportCommentTYPE";
    public static final String KEY_REPORT_COMMENT_ORJ_ID = "reportCommentORJ_ID";
    public static final String KEY_REPORT_COMMENT_TEXT = "reportCommentTEXT";
    public static final String KEY_REPORT_COMMENT_UserID = "reportCommentUserID";
    public static final String KEY_REPORT_COMMENT_RUserID = "reportCommentRUserID";
    public static final String KEY_REPORT_COMMENT_DATE = "reportCommentDATE";
    public static final String KEY_REPORT_COMMENT_ObjectID = "reportCommentObjectID";
    public static final String KEY_REPORT_COMMENT_STATUS = "reportCommentSTATUS";

    public static final String KEY_REPORT_QUESTION_COLLECTIONS = "reportQuestions";
    public static final String KEY_REPORT_QUESTION_ID = "questionID";
    public static final String KEY_REPORT_QUESTION_ORJ_ID = "questionORJ_ID";
    public static final String KEY_REPORT_QUESTION_TITLE = "questionTITLE";
    public static final String KEY_REPORT_QUESTION_TEXT = "questionTEXT";
    public static final String KEY_REPORT_QUESTION_DATE = "questionDATE";
    public static final String KEY_REPORT_QUESTION_UserID = "questionUserID";
    public static final String KEY_REPORT_QUESTION_RUserID = "questionRUserID";
    public static final String KEY_REPORT_QUESTION_STATUS = "questionSTATUS";

    public static final String KEY_SCHOOL_CLUB_COLLECTIONS = "schoolClubs";
    public static final String KEY_SCHOOL_CLUB_ID = "schoolClubID";
    public static final String KEY_SCHOOL_CLUB_OWNER = "schoolClubOWNER";
    public static final String KEY_SCHOOL_CLUB_MAIL = "schoolClubMAIL";
    public static final String KEY_SCHOOL_CLUB_WhatsappADDRESS = "schoolClubWhatsappADDRESS";
    public static final String KEY_SCHOOL_CLUB_InstagramADDRESS = "schoolClubInstagramADDRESS";
    public static final String KEY_SCHOOL_CLUB_FacebookADDRESS = "schoolClubFacebookADDRESS";
    public static final String KEY_SCHOOL_CLUB_TwitterADDRESS = "schoolClubTwitterADDRESS";
    public static final String KEY_SCHOOL_CLUB_IMAGE = "schoolClubIMAGE";
    public static final String KEY_SCHOOL_CLUB_DATE = "schoolClubDATE";
    public static final String KEY_SCHOOL_CLUB_TITLE = "schoolClubTITLE";
    public static final String KEY_SCHOOL_CLUB_DESC = "schoolClubDESC";
    public static final String KEY_SCHOOL_CLUB_STATUS = "schoolClubSTATUS";

    public static final String KEY_VOTE_COLLECTIONS = "votes";
    public static final String KEY_VOTE_ID = "voteID";
    public static final String KEY_VOTE_Object_ID = "voteObjectID";
    public static final String KEY_VOTE_TYPE = "voteTYPE";
    public static final String KEY_VOTE_STATUS = "voteSTATUS";
    public static final String KEY_VOTE_UserID = "voteUserID";

    public static final String KEY_SCHOOL_DORMITORY_COLLECTIONS = "schoolDormitories";
    public static final String KEY_SCHOOL_DORMITORY_ID = "schoolDormitoryID";
    public static final String KEY_SCHOOL_DORMITORY_TITLE = "schoolDormitoryTITLE";
    public static final String KEY_SCHOOL_DORMITORY_ADDRESS = "schoolDormitoryADDRESS";
    public static final String KEY_SCHOOL_DORMITORY_TYPE = "schoolDormitoryTYPE";
    public static final String KEY_SCHOOL_DORMITORY_BED_NUMBERS = "schoolDormitoryBedNUMBERS";
    public static final String KEY_SCHOOL_DORMITORY_PRICE = "schoolDormitoryPRICE";
    public static final String KEY_SCHOOL_DORMITORY_WEB_ADDRESS = "schoolDormitoryWebADDRESS";
    public static final String KEY_SCHOOL_DORMITORY_TELEPHONE_NUMBER = "schoolDormitoryTelephoneNUMBER";
    public static final String KEY_SCHOOL_DORMITORY_WIFI = "schoolDormitoryWIFI";
    public static final String KEY_SCHOOL_DORMITORY_MEAL = "schoolDormitoryMEAL";
    public static final String KEY_SCHOOL_DORMITORY_STATUS = "schoolDormitorySTATUS";

    public static final String KEY_CONFESSION_COLLECTIONS = "confessions";
    public static final String KEY_CONFESSION_ID = "confessionID";
    public static final String KEY_CONFESSION_CONTENT = "confessionCONTENT";
    public static final String KEY_CONFESSION_DATE = "confessionDATE";
    public static final String KEY_CONFESSION_STATUS = "confessionSTATUS";
    public static final String KEY_CONFESSION_UserID = "confessionUserID";

    public static final String KEY_REPORT_CONFESSION_COLLECTIONS = "reportConfessions";
    public static final String KEY_REPORT_CONFESSION_ID = "confessionID";
    public static final String KEY_REPORT_CONFESSION_CONTENT = "confessionCONTENT";
    public static final String KEY_REPORT_CONFESSION_DATE = "confessionDATE";
    public static final String KEY_REPORT_CONFESSION_STATUS = "confessionSTATUS";
    public static final String KEY_REPORT_CONFESSION_UserID = "confessionUserID";
}
