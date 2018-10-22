package com.shivomthakkar.miniproject;

/**
 * Created by Shivom on 10/17/17.
 */
public class Student {

    int rollNo;
    String name, imei, time, stClass;

    Student(int rollNo, String name, String imei, String time, String stClass) {
        this.rollNo = rollNo;
        this.name = name;
        this.imei = imei;
        this.time = time;
        this.stClass = stClass;

    }

    int getRollNo() {
        return rollNo;
    }

    String getName() {
        return name;
    }

    String getImei(){
        return imei;
    }

    String getTime() {
        return time;
    }

    String getStClass() {
        return stClass;
    }

    void setTime(String time) {
        this.time = time;
    }

    void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    void setName(String name) {
        this.name = name;
    }

    void setImei(String imei) {
        this.imei = imei;
    }

}
