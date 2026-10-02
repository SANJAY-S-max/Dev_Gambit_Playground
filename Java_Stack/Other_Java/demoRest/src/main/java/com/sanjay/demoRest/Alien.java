package com.sanjay.demoRest;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Alien {

    private int id;
    private String name;
    private int points;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Alien [id=" + id + ", name=" + name + ", points=" + points + "]";
    }
}