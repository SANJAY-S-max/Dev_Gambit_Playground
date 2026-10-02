package com.sanjay.Students;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Laptop {

    @Id
    private int lid;
    private String lname;
    private int price;

    public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	// Many Laptops can belong to One Student
    @ManyToOne
    @JoinColumn(name = "student_roll") // Creates Foreign Key column inside Laptop table
    private Student student;

    public int getLid() {
        return lid;
    }
    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
	public String toString() {
		return "Laptop [lid=" + lid + ", lname=" + lname + ", price=" + price + ", student=" + student + "]";
	}
}


























//package com.sanjay.Students;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//
//@Entity
//public class Laptop {
//
//    @Id
//    private int lid;
//    private String lname;
//    private int price;
//
//    public int getPrice() {
//		return price;
//	}
//	public void setPrice(int price) {
//		this.price = price;
//	}
//
//	// Many Laptops can belong to One Student
//    @ManyToOne
//    @JoinColumn(name = "student_roll") // Creates Foreign Key column inside Laptop table
//    private Student student;
//
//    public int getLid() {
//        return lid;
//    }
//    public void setLid(int lid) {
//        this.lid = lid;
//    }
//
//    public String getLname() {
//        return lname;
//    }
//    public void setLname(String lname) {
//        this.lname = lname;
//    }
//
//    public Student getStudent() {
//        return student;
//    }
//    public void setStudent(Student student) {
//        this.student = student;
//    }
//
//    @Override
//	public String toString() {
//		return "Laptop [lid=" + lid + ", lname=" + lname + ", price=" + price + ", student=" + student + "]";
//	}
//}