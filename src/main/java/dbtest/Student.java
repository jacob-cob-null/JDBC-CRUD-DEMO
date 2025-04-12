/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbtest;

/**
 *
 * @author Jacob
 */
public class Student {

    int studentID;
    String studentName;
    int studentAge;

    //complete attributes
    public Student(int studentID, String studentName, int studentAge) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentAge = studentAge;
    }
    //Creating records
    public Student(String studentName, int studentAge) {
        this.studentName = studentName;
        this.studentAge = studentAge;
    }

    //only id
    public Student(int studentID) {
        this.studentID = studentID;
    }

    //GETTERS AND SETTERS
      public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(int studentAge) {
        this.studentAge = studentAge;
    }
    
    @Override
    public String toString() {
        return "Student ID: " + this.studentID
                + "\nName: " + this.studentName
                + "\nAge: " + this.studentAge;
    }
}
