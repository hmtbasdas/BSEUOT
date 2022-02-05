package com.hmtbasdas.bseuot.Models;

public class Vote {
    private final String voteID;
    private final String voteObjectID;
    private final String voteTYPE;
    private final String voteUserID;
    private final Boolean voteSTATUS;

    public Vote(String voteID, String voteObjectID, String voteTYPE, String voteUserID, Boolean voteSTATUS) {
        this.voteID = voteID;
        this.voteObjectID = voteObjectID;
        this.voteTYPE = voteTYPE;
        this.voteUserID = voteUserID;
        this.voteSTATUS = voteSTATUS;
    }

    public String getVoteID() {
        return voteID;
    }

    public String getVoteObjectID() {
        return voteObjectID;
    }

    public String getVoteTYPE() {
        return voteTYPE;
    }

    public String getVoteUserID() {
        return voteUserID;
    }

    public Boolean getVoteSTATUS() {
        return voteSTATUS;
    }
}
