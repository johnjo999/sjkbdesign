package com.sjkb.models.jobs;

public class AddQuoteModel extends AddNoteModel {
    private int amount;
    private int installerQ;
    private int cabinetQ;

    public AddQuoteModel() {}

    public AddQuoteModel(String jobid) {
        super(jobid);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInstallerQ() {
        return installerQ;
    }

    public void setInstallerQ(int installerQ) {
        this.installerQ = installerQ;
    }

    public int getCabinetQ() {
        return cabinetQ;
    }

    public void setCabinetQ(int cabinetQ) {
        this.cabinetQ = cabinetQ;
    }

}