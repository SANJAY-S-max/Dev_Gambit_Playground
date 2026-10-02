package com.sanjay.demoRest;

import java.util.Set; 

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/webapi")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
            MyResource.class,
            AlienResource.class
        );
    }
}