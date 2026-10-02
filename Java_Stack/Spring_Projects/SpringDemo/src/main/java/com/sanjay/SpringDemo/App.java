package com.sanjay.SpringDemo;

import org.springframework.beans.factory.BeanFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	BeanFactory ft = new XmlBeanFactory(resource);
//        System.out.println( "Hello World!" );
    	Alien obj = ft.getBean(Alien.class);
    }
}
