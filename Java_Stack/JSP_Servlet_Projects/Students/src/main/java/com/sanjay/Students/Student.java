package com.sanjay.Students;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Student {

    @Id
    private int rollno; 
    private String name;
    private int marks;

    // mappedBy refers to the 'student' field inside Laptop.java
    @OneToMany(mappedBy = "student")
    private List<Laptop> laptop= new ArrayList<>();

    public List<Laptop> getLaptop() {
        return laptop;
    }
    public void setLaptop(List<Laptop> laptop) {
        this.laptop = laptop;
    }

    public int getRollno() {
        return rollno;
    }
    public void setRollno(int rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getMarks() {
        return marks;
    }
    public void setMarks(int marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Student [rollno=" + rollno + ", name=" + name + ", marks=" + marks + ", laptop=" + laptop + "]";
    }
}






















//package com.sanjay.Students;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//
//@Entity
//public class Student {
//
//    @Id
//    private int rollno; 
//    private String name;
//    private int marks;
//
//    // mappedBy refers to the 'student' field inside Laptop.java
//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    private List<Laptop> laptop= new ArrayList<>();
//
//    public List<Laptop> getLaptop() {
//        return laptop;
//    }
//    public void setLaptop(List<Laptop> laptop) {
//        this.laptop = laptop;
//    }
//
//    public int getRollno() {
//        return rollno;
//    }
//    public void setRollno(int rollno) {
//        this.rollno = rollno;
//    }
//
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getMarks() {
//        return marks;
//    }
//    public void setMarks(int marks) {
//        this.marks = marks;
//    }
//
//    @Override
//    public String toString() {
//        return "Student [rollno=" + rollno + ", name=" + name + ", marks=" + marks + ", laptop=" + laptop + "]";
//    }
//}


