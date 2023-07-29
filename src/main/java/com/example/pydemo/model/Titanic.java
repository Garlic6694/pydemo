package com.example.pydemo.model;

import lombok.Data;

public class Titanic {
    private int PassengerId;
    private String Survived;
    private String Pclass;
    private String Name;
    private String Sex;
    private int Age;
    private String SibSp;
    private int Parch;
    private String Ticket;
    private String Fare;
    private String Cabin;
    private String Embarked;

    public int getPassengerId() {
        return PassengerId;
    }

    public void setPassengerId(int passengerId) {
        PassengerId = passengerId;
    }

    public String getSurvived() {
        return Survived;
    }

    public void setSurvived(String survived) {
        Survived = survived;
    }

    public String getPclass() {
        return Pclass;
    }

    public void setPclass(String pclass) {
        Pclass = pclass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getSibSp() {
        return SibSp;
    }

    public void setSibSp(String sibSp) {
        SibSp = sibSp;
    }

    public int getParch() {
        return Parch;
    }

    public void setParch(int parch) {
        Parch = parch;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getCabin() {
        return Cabin;
    }

    public void setCabin(String cabin) {
        Cabin = cabin;
    }

    public String getEmbarked() {
        return Embarked;
    }

    public void setEmbarked(String embarked) {
        Embarked = embarked;
    }

    @Override
    public String toString() {
        return "Titanic{" +
                "PassengerId=" + PassengerId +
                ", Survived='" + Survived + '\'' +
                ", Pclass='" + Pclass + '\'' +
                ", Name='" + Name + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Age=" + Age +
                ", SibSp='" + SibSp + '\'' +
                ", Parch=" + Parch +
                ", Ticket='" + Ticket + '\'' +
                ", Fare='" + Fare + '\'' +
                ", Cabin='" + Cabin + '\'' +
                ", Embarked='" + Embarked + '\'' +
                '}';
    }
}
