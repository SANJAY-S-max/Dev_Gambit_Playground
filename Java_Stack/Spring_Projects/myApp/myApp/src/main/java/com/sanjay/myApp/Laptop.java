package com.sanjay.myApp;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class Laptop implements Computer{
    public void compile(){
        System.out.println("Compile with 404 bugs");
    }
}
