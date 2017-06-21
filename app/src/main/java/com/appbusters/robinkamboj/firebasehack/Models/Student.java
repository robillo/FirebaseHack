package com.appbusters.robinkamboj.firebasehack.Models;

public class Student {
    private String name;
    private String email;
    private String bio;
    private String photoUrl;
    private String DOB;
    private String gender;
    private String phone;
    private String college;
    private String batch;
    private String studentUid;

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Student(String name, String email, String bio, String photoUrl, String DOB, String gender, String phone, String college, String batch) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.photoUrl = photoUrl;
        this.DOB = DOB;
        this.gender = gender;
        this.phone = phone;
        this.college = college;
        this.batch = batch;
    }

    public Student(){
        //Empty Constructor
    }
}
