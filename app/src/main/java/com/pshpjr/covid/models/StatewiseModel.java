package com.pshpjr.covid.models;

public class StatewiseModel {
    private String state;//도시
    private String confirmed;//확진
    private String active;//확진률
    private String deceased;//사망
    private String newConfirmed;//신규확진
    private String newRecovered;
    private String newDeceased;
    private String lastupdate;
    private String recovered;//완치자

    public StatewiseModel(String state, String confirmed, String active, String deceased, String newConfirmed, String newRecovered, String newDeceased, String lastupdate, String recovered) {
        this.state = state;
        this.confirmed = confirmed;
        this.active = active;
        this.deceased = deceased;
        this.newConfirmed = newConfirmed;
        this.newRecovered = newRecovered;
        this.newDeceased = newDeceased;
        this.lastupdate = lastupdate;
        this.recovered = recovered;
    }

    public String getState() {
        return state;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getActive() {
        return active;
    }

    public String getDeceased() {
        return deceased;
    }

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public String getNewRecovered() {
        return newRecovered;
    }

    public String getNewDeceased() {
        return newDeceased;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public String getRecovered() {
        return recovered;
    }
}
