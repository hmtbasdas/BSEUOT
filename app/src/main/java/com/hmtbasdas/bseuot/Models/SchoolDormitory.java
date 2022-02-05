package com.hmtbasdas.bseuot.Models;

import java.io.Serializable;

public class SchoolDormitory implements Serializable {

    private String dormitoryID;
    private String dormitoryTITLE;
    private String dormitoryADDRESS;
    private String dormitoryTYPE;
    private String dormitoryBedNUMBERS;
    private String dormitoryPRICE;
    private String dormitoryMeal;
    private String dormitoryWebADDRESS;
    private String dormitoryTelephoneNUMBER;

    private Boolean dormitoryWIFI;
    private Boolean dormitorySTATUS;

    public SchoolDormitory(String dormitoryID, String dormitoryTITLE, String dormitoryADDRESS, String dormitoryTYPE, String dormitoryBedNUMBERS, String dormitoryPRICE, String dormitoryMeal, String dormitoryWebADDRESS, String dormitoryTelephoneNUMBER, Boolean dormitoryWIFI, Boolean dormitorySTATUS) {
        this.dormitoryID = dormitoryID;
        this.dormitoryTITLE = dormitoryTITLE;
        this.dormitoryADDRESS = dormitoryADDRESS;
        this.dormitoryTYPE = dormitoryTYPE;
        this.dormitoryBedNUMBERS = dormitoryBedNUMBERS;
        this.dormitoryPRICE = dormitoryPRICE;
        this.dormitoryMeal = dormitoryMeal;
        this.dormitoryWebADDRESS = dormitoryWebADDRESS;
        this.dormitoryTelephoneNUMBER = dormitoryTelephoneNUMBER;
        this.dormitoryWIFI = dormitoryWIFI;
        this.dormitorySTATUS = dormitorySTATUS;
    }

    public String getDormitoryID() {
        return dormitoryID;
    }

    public String getDormitoryTITLE() {
        return dormitoryTITLE;
    }

    public String getDormitoryADDRESS() {
        return dormitoryADDRESS;
    }

    public String getDormitoryTYPE() {
        return dormitoryTYPE;
    }

    public String getDormitoryBedNUMBERS() {
        return dormitoryBedNUMBERS;
    }

    public String getDormitoryPRICE() {
        return dormitoryPRICE;
    }

    public String getDormitoryMeal() {
        return dormitoryMeal;
    }

    public String getDormitoryWebADDRESS() {
        return dormitoryWebADDRESS;
    }

    public String getDormitoryTelephoneNUMBER() {
        return dormitoryTelephoneNUMBER;
    }

    public Boolean getDormitoryWIFI() {
        return dormitoryWIFI;
    }

    public Boolean getDormitorySTATUS() {
        return dormitorySTATUS;
    }
}
