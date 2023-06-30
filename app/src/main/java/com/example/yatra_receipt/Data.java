package com.example.yatra_receipt;

import io.realm.RealmObject;

public class Data extends RealmObject {
    long receiptNo;
    String name, gam, mobileNo, tithiYatra, people, children, amount, deposit, baki, svikarnar;
    String createdData;
    long createdTime;

    public long getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(int receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGam() {
        return gam;
    }

    public void setGam(String gam) {
        this.gam = gam;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTithiYatra() {
        return tithiYatra;
    }

    public void setTithiYatra(String tithiYatra) {
        this.tithiYatra = tithiYatra;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getBaki() {
        return baki;
    }

    public String getSvikarnar() {
        return svikarnar;
    }

    public void setSvikarnar(String svikarnar) {
        this.svikarnar = svikarnar;
    }

    public void setBaki(String baki) {
        this.baki = baki;
    }

    public String getCreatedData() {
        return createdData;
    }

    public void setCreatedData(String createdData) {
        this.createdData = createdData;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
