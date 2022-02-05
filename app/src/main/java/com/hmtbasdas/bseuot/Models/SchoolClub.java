package com.hmtbasdas.bseuot.Models;

import java.io.Serializable;

public class SchoolClub implements Serializable {
    private final String clubID;
    private final String clubWhatsappADDRESS;
    private final String clubInstagramADDRESS;
    private final String clubFacebookADDRESS;
    private final String clubTwitterADDRESS;
    private final String clubIMAGE;
    private final String clubDATE;
    private final String clubTITLE;
    private final String clubDESC;

    private final Boolean clubSTATUS;

    public SchoolClub(String clubID, String clubWhatsappADDRESS, String clubInstagramADDRESS, String clubFacebookADDRESS, String clubTwitterADDRESS, String clubIMAGE, String clubDATE, String clubTITLE, String clubDESC, Boolean clubSTATUS) {
        this.clubID = clubID;
        this.clubWhatsappADDRESS = clubWhatsappADDRESS;
        this.clubInstagramADDRESS = clubInstagramADDRESS;
        this.clubFacebookADDRESS = clubFacebookADDRESS;
        this.clubTwitterADDRESS = clubTwitterADDRESS;
        this.clubIMAGE = clubIMAGE;
        this.clubDATE = clubDATE;
        this.clubTITLE = clubTITLE;
        this.clubDESC = clubDESC;
        this.clubSTATUS = clubSTATUS;
    }

    public String getClubID() {
        return clubID;
    }

    public String getClubWhatsappADDRESS() {
        return clubWhatsappADDRESS;
    }

    public String getClubInstagramADDRESS() {
        return clubInstagramADDRESS;
    }

    public String getClubFacebookADDRESS() {
        return clubFacebookADDRESS;
    }

    public String getClubTwitterADDRESS() {
        return clubTwitterADDRESS;
    }

    public String getClubIMAGE() {
        return clubIMAGE;
    }

    public String getClubDATE() {
        return clubDATE;
    }

    public String getClubTITLE() {
        return clubTITLE;
    }

    public String getClubDESC() {
        return clubDESC;
    }

    public Boolean getClubSTATUS() {
        return clubSTATUS;
    }
}
