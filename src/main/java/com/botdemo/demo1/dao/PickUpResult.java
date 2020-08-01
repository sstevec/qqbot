package com.botdemo.demo1.dao;


public class PickUpResult {
    private int stoneNumber;
    private int oneNumber;
    private int twoNumber;
    private int threeNumber;
    private int rateUpNumber;
    private int firstAppear;

    public PickUpResult(){
        this.stoneNumber = 0;
        this.oneNumber = 0;
        this.twoNumber = 0;
        this.threeNumber = 0;
        this.rateUpNumber = 0;
        this.firstAppear = -1;
    }

    public int getFirstAppear() {
        return firstAppear;
    }

    public void setFirstAppear(int firstAppear) {
        this.firstAppear = firstAppear;
    }

    public int getStoneNumber() {
        return stoneNumber;
    }

    public void addStoneNumber(int stoneNumber) {
        this.stoneNumber += stoneNumber;
    }

    public int getThreeNumber() {
        return threeNumber;
    }

    public void addThreeNumber(int threeNumber) {
        this.threeNumber += threeNumber;
    }

    public int getRateUpNumber() {
        return rateUpNumber;
    }

    public void addRateUpNumber(int rateUpNumber) {
        this.rateUpNumber += rateUpNumber;
    }

    public int getOneNumber() {
        return oneNumber;
    }

    public void addOneNumber(int oneNumber) {
        this.oneNumber += oneNumber;
    }

    public int getTwoNumber() {
        return twoNumber;
    }

    public void addTwoNumber(int twoNumber) {
        this.twoNumber += twoNumber;
    }

    public void appendResult(PickUpResult old){
        this.stoneNumber += old.stoneNumber;
        this.oneNumber += old.oneNumber;
        this.twoNumber += old.twoNumber;
        this.threeNumber += old.threeNumber;
        this.rateUpNumber += old.rateUpNumber;
    }
}
