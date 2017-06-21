package com.appbusters.robinkamboj.firebasehack.Utils;

import com.appbusters.robinkamboj.firebasehack.Models.Student;
import com.appbusters.robinkamboj.firebasehack.Models.Teacher;

/**
 * Created by aditya on 19/04/17.
 */

public class Singleton {

    public static final String TAG = Singleton.class.getSimpleName();

    private static Singleton singleton;
    private Student student = new Student();
    private Teacher teacher = new Teacher();
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    private Singleton()
    {
        student = new Student();
    }

    public static synchronized Singleton getInstance() {
        if (singleton == null) //if none created
            singleton = new Singleton(); //create rate_one
        return singleton; //return it
    }

    public Student getStudent() {return student;}

    public void setStudent(Student student) {this.student = student;}

    public double getCurrentLatitude() {return currentLatitude;}

    public void setCurrentLatitude(double currentLatitude) {this.currentLatitude = currentLatitude;}

    public double getCurrentLongitude() {return currentLongitude;}

    public void setCurrentLongitude(double currentLongitude) {this.currentLongitude = currentLongitude;}
}
