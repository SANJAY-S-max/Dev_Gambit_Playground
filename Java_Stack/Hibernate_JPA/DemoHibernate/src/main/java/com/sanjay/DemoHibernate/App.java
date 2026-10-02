package com.sanjay.DemoHibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
public class App 
{
    public static void main( String[] args )
    {
//        System.out.println( "Hello World!" );
    	AlienName aname = new AlienName();
    	aname.setFname("Sanjay");
    	aname.setLname("S");
    	aname.setMname("Kumar");
    	
    	Alien obj = new Alien();
    	obj.setAid(101);
    	obj.setAname(aname);
    	obj.setColor("green");
    	
    	
    	
    	StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
    	        .configure() // loads hibernate.cfg.xml
    	        .build();

    	Metadata metadata = new MetadataSources(registry)
    	        .getMetadataBuilder()
    	        .build();

    	SessionFactory sf = metadata.getSessionFactoryBuilder().build();
    	Session session=sf.openSession();
    	Transaction tx = session.beginTransaction();
    	session.persist(obj);
//    	obj = session.get(Alien.class,122);
    	tx.commit();
//    	System.out.print(obj);
    	session.close();
        sf.close();
        
        System.out.println("Data saved successfully!");
    }
}
