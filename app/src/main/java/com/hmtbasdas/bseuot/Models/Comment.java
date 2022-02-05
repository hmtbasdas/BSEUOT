package com.hmtbasdas.bseuot.Models;

public class Comment {
    private String commentID;
    private String commentTYPE;
    private String commentDATE;
    private String commentTEXT;
    private String commentUserID;
    private String commentUserNAME;
    private String commentUserIMAGE;
    private String commentObjectID;
    private Boolean commentSTATUS;

    public Comment(){

    }
    public Comment(String commentID, String commentTYPE, String commentDATE, String commentTEXT, String commentUserID, String commentUserNAME, String commentUserIMAGE, String commentObjectID, Boolean commentSTATUS) {
        this.commentID = commentID;
        this.commentTYPE = commentTYPE;
        this.commentDATE = commentDATE;
        this.commentTEXT = commentTEXT;
        this.commentUserID = commentUserID;
        this.commentUserNAME = commentUserNAME;
        this.commentUserIMAGE = commentUserIMAGE;
        this.commentObjectID = commentObjectID;
        this.commentSTATUS = commentSTATUS;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setCommentTYPE(String commentTYPE) {
        this.commentTYPE = commentTYPE;
    }

    public void setCommentDATE(String commentDATE) {
        this.commentDATE = commentDATE;
    }

    public void setCommentTEXT(String commentTEXT) {
        this.commentTEXT = commentTEXT;
    }

    public void setCommentUserID(String commentUserID) {
        this.commentUserID = commentUserID;
    }

    public void setCommentUserNAME(String commentUserNAME) {
        this.commentUserNAME = commentUserNAME;
    }

    public void setCommentUserIMAGE(String commentUserIMAGE) {
        this.commentUserIMAGE = commentUserIMAGE;
    }

    public void setCommentObjectID(String commentObjectID) {
        this.commentObjectID = commentObjectID;
    }

    public void setCommentSTATUS(Boolean commentSTATUS) {
        this.commentSTATUS = commentSTATUS;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommentTYPE() {
        return commentTYPE;
    }

    public String getCommentDATE() {
        return commentDATE;
    }

    public String getCommentTEXT() {
        return commentTEXT;
    }

    public String getCommentUserID() {
        return commentUserID;
    }

    public String getCommentUserNAME() {
        return commentUserNAME;
    }

    public String getCommentUserIMAGE() {
        return commentUserIMAGE;
    }

    public String getCommentObjectID() {
        return commentObjectID;
    }

    public Boolean getCommentSTATUS() {
        return commentSTATUS;
    }

}
