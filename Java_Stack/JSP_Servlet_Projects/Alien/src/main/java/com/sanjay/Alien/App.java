package com.sanjay.Alien;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class App {

    public static void main(String[] args) {

        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

        Metadata metadata =
                new MetadataSources(registry)
                        .getMetadataBuilder()
                        .build();

        SessionFactory sf =
                metadata.getSessionFactoryBuilder()
                        .build();
        
        
//        Session session = sf.openSession();
//
//        Transaction tx = session.beginTransaction();
//
//        AlienName name = new AlienName();
//        name.setFname("Sanjay");
//        name.setMname("Kumar");
//        name.setLname("B");
//
//        Alien alien = new Alien();
//        alien.setAid(101);
//        alien.setAname(name);
//        alien.setColor("Black");
//
//        session.persist(alien);
//
//        tx.commit();
//        session.close();
//

        Session session1 = sf.openSession();

        Transaction tx1 = session1.beginTransaction();
        Query q1 = session1.createQuery("from Alien where aid = 101");
//        Alien a1 = session1.find(Alien.class, 101);
        q1.setCacheable(true);
        Alien a1 = (Alien)q1.uniqueResult();
        System.out.println(a1);
        tx1.commit();
        session1.close();

        Session session2 = sf.openSession();

        Transaction tx2 = session2.beginTransaction();
        Query q2 = session2.createQuery("from Alien where aid = 101");
        q2.setCacheable(true);
        Alien a2 = (Alien)q2.uniqueResult();
        
//        Alien a2 = session2.find(Alien.class, 101);
        System.out.println(a2);

        tx2.commit();
        session2.close();

        sf.close();

        StandardServiceRegistryBuilder.destroy(registry);
    }
}
