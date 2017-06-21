package com.appbusters.robinkamboj.firebasehack.Models;

/**
 * Created by rishabhshukla on 21/06/17.
 */

public class Teacher {
    private String name;
    private String photoUrl;
    private Integer age;
    private String gender;
    private String phone;
    private String college;
    private String subject;
    private String uid;
    private String fbId;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher(String name, String photoUrl, Integer age, String gender, String phone, String college, String subject, String fbId) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.college = college;
        this.subject = subject;
        this.fbId = fbId;
    }

    public Integer getAge() {

        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Teacher(){
        //Empty Constructor
    }
}
