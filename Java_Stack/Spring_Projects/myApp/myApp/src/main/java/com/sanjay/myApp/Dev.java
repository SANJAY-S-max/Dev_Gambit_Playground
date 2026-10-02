package com.sanjay.myApp;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Dev {
    @Autowired //Flied Injection
    @Qualifier("laptop")
    private Computer com=null;
//    Dev(Laptop lap){
//        this.lap = new Laptop();
//    }

//    @Autowired
//    public void setLaptop(Laptop lap){
//        this.com = lap;
//    }
//    private Laptop lap;
    public void build(){
        com.compile();
        System.out.println("Working on Awesome Project");
    }
}
