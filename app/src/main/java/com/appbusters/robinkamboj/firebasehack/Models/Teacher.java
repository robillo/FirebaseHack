package com.appbusters.robinkamboj.firebasehack.Models;

/**
 * Created by rishabhshukla on 21/06/17.
 */

public class Teacher {
    private String name;
    private String email;
    private String bio;
    private String photoUrl;
    private String DOB;
    private String gender;
    private String phone;
    private String college;
    private String subject;
    private String teacherUid;

    public Teacher(String name, String email, String bio, String photoUrl, String DOB, String gender, String phone, String college, String subject) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.photoUrl = photoUrl;
        this.DOB = DOB;
        this.gender = gender;
        this.phone = phone;
        this.college = college;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherUid() {
        return teacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        this.teacherUid = teacherUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
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
