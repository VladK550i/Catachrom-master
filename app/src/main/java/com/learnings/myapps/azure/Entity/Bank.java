package com.learnings.myapps.azure.Entity;


public class Bank {
    public String getBankName() {
        return BankName;
    }
    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankRecId() {
        return BankRecId;
    }
    public void setBankRecId(String bankRecId) {
        BankRecId = bankRecId;
    }

    public String getBankDescription() {
        return BankDescription;
    }
    public void setBankDescription(String bankDescription) {
        BankDescription = bankDescription;
    }

    public int getLastQuartalDepositSum() {
        return LastQuartalDepositSum;
    }
    public void setLastQuartalDepositSum(int lastQuartalDepositSum) {
        LastQuartalDepositSum = lastQuartalDepositSum;
    }

    public int getLastQuartalCreditSum() {
        return LastQuartalCreditSum;
    }
    public void setLastQuartalCreditSum(int lastQuartalCreditSum) {
        LastQuartalCreditSum = lastQuartalCreditSum;
    }

    public int getLastQuartalLiquidActive() {
        return LastQuartalLiquidActive;
    }
    public void setLastQuartalLiquidActive(int lastQuartalLiquidActive) {
        LastQuartalLiquidActive = lastQuartalLiquidActive;
    }

    public int getLastQuartalAuthorisedCapital() {
        return LastQuartalAuthorisedCapital;
    }
    public void setLastQuartalAuthorisedCapital(int lastQuartalAuthorisedCapital) {
        LastQuartalAuthorisedCapital = lastQuartalAuthorisedCapital;
    }

    public String id;
    private String BankRecId;
    private String BankName;
    private String BankDescription;
    private int LastQuartalDepositSum;
    private int LastQuartalCreditSum;
    private int LastQuartalLiquidActive;
    private int LastQuartalAuthorisedCapital;

}
