package com.hmtbasdas.bseuot.Models;

import java.io.Serializable;

public class Confession implements Serializable {

    private final String confessionID;
    private final String confessionDATE;
    private final String confessionCONTENT;
    private final String confessionUserID;

    private final Boolean confessionSTATUS;

    public Confession(String confessionID, String confessionDATE, String confessionCONTENT, String confessionUserID, Boolean confessionSTATUS) {
        this.confessionID = confessionID;
        this.confessionDATE = confessionDATE;
        this.confessionCONTENT = confessionCONTENT;
        this.confessionUserID = confessionUserID;
        this.confessionSTATUS = confessionSTATUS;
    }

    public String getConfessionID() {
        return confessionID;
    }

    public String getConfessionDATE() {
        return confessionDATE;
    }

    public String getConfessionCONTENT() {
        return confessionCONTENT;
    }

    public String getConfessionUserID() {
        return confessionUserID;
    }
    public Boolean getConfessionSTATUS() {
        return confessionSTATUS;
    }
}
