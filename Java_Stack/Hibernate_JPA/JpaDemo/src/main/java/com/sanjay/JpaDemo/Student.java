package com.sanjay.JpaDemo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {
	private String name;
	@Id
	private int rollno;
	private String tech;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRollno() {
		return rollno;
	}
	public void setRollno(int rollno) {
		this.rollno = rollno;
	}
	public String getTech() {
		return tech;
	}
	public void setTech(String tech) {
		this.tech = tech;
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", rollno=" + rollno + ", tech=" + tech + "]";
	}
	
}
