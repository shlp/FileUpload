package com.example.shenliping.mytesttwo;

/**
 * Created by shenliping on 2018/11/8.
 */

public class Student {
    private int id;
    private String name;
    private String number;
    private String sex;
    private String hobby;
    private String city;

    private Boolean checked;
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
    public int getId() {
        return id;
    }
    public String getHobby() {
        return hobby;
    }
    public String getSex() {
        return sex;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
    public String getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
    public Student(){}
    public Student(String name, String number, String hobby, String sex, String city) {
        this.name = name;
        this.number = number;
        this.hobby = hobby;
        this.sex = sex;
        this.city = city;
    }
}
