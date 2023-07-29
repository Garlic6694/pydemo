package com.example.pydemo.model;

import lombok.Data;

@Data
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

}
