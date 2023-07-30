package com.example.pydemo.model;

import lombok.Data;

@Data
public class Titanic {
    private int Id; // 乘客id
    private String Survived; // 存活与否
    private String Pclass; // 乘客等级
    private String Name; //
    private String Sex;
    private int Age;
    private String SibSp; //兄弟姐妹个数
    private int Parch; // 父母小孩的个数
    private String Ticket;
    private String Fare; // 票价
    private String Cabin; // 船舱
    private String Embarked; //登船港口

}
