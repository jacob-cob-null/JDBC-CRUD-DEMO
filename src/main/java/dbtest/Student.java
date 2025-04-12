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
    public Student(int studentID, String studentName, int studentAge){
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentAge = studentAge;
    }
    //only id
    public Student(int studentID) {
        this.studentID = studentID;
    }
}
