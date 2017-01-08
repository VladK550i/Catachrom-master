package com.learnings.myapps.azure.Entity;

import java.util.Date;


public class BankOffer {

    public String getOfferName() {
        return OfferName;
    }
    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getCurrency() {
        return Currency;
    }
    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCapitalize() {
        return Capitalize;
    }
    public void setCapitalize(String capitalize) {
        Capitalize = capitalize;
    }

    public boolean isWithReplenishment() {
        return withReplenishment;
    }
    public void setWithReplenishment(boolean withReplenishment) {
        this.withReplenishment = withReplenishment;
    }

    public boolean isWithPartialCashOut() {
        return withPartialCashOut;
    }
    public void setWithPartialCashOut(boolean withPartialCashOut) {
        this.withPartialCashOut = withPartialCashOut;
    }

    public Date getActiveFrom() {
        return ActiveFrom;
    }
    public void setActiveFrom(Date activeFrom) {
        ActiveFrom = activeFrom;
    }

    public Date getActiveTo() {
        return ActiveTo;
    }
    public void setActiveTo(Date activeTo) {
        ActiveTo = activeTo;
    }

    public double getInterestRate() {
        return InterestRate;
    }
    public void setInterestRate(double interestRate) {
        InterestRate = interestRate;
    }

    public String getInterestPeriodicity() {
        return InterestPeriodicity;
    }
    public void setInterestPeriodicity(String interestPeriodicity) {
        InterestPeriodicity = interestPeriodicity;
    }

    public double getEarlInterestRate() {
        return EarlInterestRate;
    }
    public void setEarlInterestRate(double earlInterestRate) {
        EarlInterestRate = earlInterestRate;
    }

    public String getBankRefRecId() {
        return BankRefRecId;
    }
    public void setBankRefRecId(String bankRefRecId) {
        BankRefRecId = bankRefRecId;
    }

    public int getEarlyTerminationTerm() {
        return EarlyTerminationTerm;
    }
    public void setEarlyTerminationTerm(int earlyTerminationTerm) {
        EarlyTerminationTerm = earlyTerminationTerm;
    }


    public String id;
    private String BankRefRecId;
    private String OfferName;
    private String Currency;  //enum inside builder
    private String Capitalize; //enum
    private boolean withReplenishment;
    private boolean withPartialCashOut;
    private Date ActiveFrom;
    private Date ActiveTo;
    private double InterestRate;
    private String InterestPeriodicity; //enum
    private double EarlInterestRate;
    private int EarlyTerminationTerm;

}
