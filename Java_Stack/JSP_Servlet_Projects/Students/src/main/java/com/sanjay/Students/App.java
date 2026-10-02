package com.sanjay.Students;

import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class App 
{
    public static void main( String[] args )
    {
        // ==================== Hibernate Execution ====================
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // Loads hibernate.cfg.xml
                .build();

        Metadata metadata = new MetadataSources(registry)
                .getMetadataBuilder()
                .build();

        SessionFactory sf = metadata.getSessionFactoryBuilder().build();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
//        Random r = new Random();
//        for(int i=1;i<=50;i++) {
//        	Student s = new Student();
//        	s.setRollno(i);
//        	s.setName("Name:"+i);
//        	s.setMarks(r.nextInt(100));
//        	session.persist(s);
//        }
        Query q =session.createQuery("from Student where rollno=7");
//        List<Student> stu = q.list();
        Student stu = (Student)q.uniqueResult();
        System.out.print(stu);
//        for(Student s:stu) {
//        	System.out.println(s);
//        }

        tx.commit();
        session.close();
        
        sf.close();

        System.out.println("Updated successfully!");
    }
}





























//package com.sanjay.Students;
//
//import java.util.Collection;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.boot.Metadata;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//
//public class App 
//{
//    public static void main( String[] args )
//    {
////        // ==================== Student 1 ====================
////        Student s1 = new Student();
////        s1.setRollno(1);
////        s1.setName("Sanjay");
////        s1.setMarks(85);
////
////        Laptop lap1 = new Laptop();
////        lap1.setLid(101);
////        lap1.setLname("Dell");
////        lap1.setStudent(s1);
////
////        Laptop lap2 = new Laptop();
////        lap2.setLid(102);
////        lap2.setLname("HP");
////        lap2.setStudent(s1);
////
////        s1.getLaptop().add(lap1);
////        s1.getLaptop().add(lap2);
////
////        // ==================== Student 2 ====================
////        Student s2 = new Student();
////        s2.setRollno(2);
////        s2.setName("Alex");
////        s2.setMarks(78);
////
////        Laptop lap3 = new Laptop();
////        lap3.setLid(103);
////        lap3.setLname("Lenovo");
////        lap3.setStudent(s2);
////
////        s2.getLaptop().add(lap3);
////
////        // ==================== Student 3 ====================
////        Student s3 = new Student();
////        s3.setRollno(3);
////        s3.setName("Priya");
////        s3.setMarks(92);
////
////        Laptop lap4 = new Laptop();
////        lap4.setLid(104);
////        lap4.setLname("MacBook");
////        lap4.setStudent(s3);
////
////        Laptop lap5 = new Laptop();
////        lap5.setLid(105);
////        lap5.setLname("Asus");
////        lap5.setStudent(s3);
////
////        s3.getLaptop().add(lap4);
////        s3.getLaptop().add(lap5);
////
////        // ==================== Student 4 ====================
////        Student s4 = new Student();
////        s4.setRollno(4);
////        s4.setName("Rahul");
////        s4.setMarks(65);
////
////        Laptop lap6 = new Laptop();
////        lap6.setLid(106);
////        lap6.setLname("Acer");
////        lap6.setStudent(s4);
////
////        s4.getLaptop().add(lap6);
////
////        // ==================== Student 5 ====================
////        Student s5 = new Student();
////        s5.setRollno(5);
////        s5.setName("Kavya");
////        s5.setMarks(88);
////
////        Laptop lap7 = new Laptop();
////        lap7.setLid(107);
////        lap7.setLname("MSI");
////        lap7.setStudent(s5);
////
////        s5.getLaptop().add(lap7);
//
//        // ==================== Hibernate Execution ====================
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                .configure() // Loads hibernate.cfg.xml
//                .build();
//
//        Metadata metadata = new MetadataSources(registry)
//                .getMetadataBuilder()
//                .build();
//
//        SessionFactory sf = metadata.getSessionFactoryBuilder().build();
//        Session session = sf.openSession();
//        Transaction tx = session.beginTransaction();
//
////         Persist each student (Cascading automatically saves all 7 laptops)
////        session.merge(s1);
////        session.merge(s2);
////        session.merge(s3);
////        session.merge(s4);
////        session.merge(s5);
//
//
//        tx.commit();
//        session.close();
//        Session session2 = sf.openSession();
//        Student fetchedStudent = session2.find(Student.class,1);
//        System.out.println(fetchedStudent.getName());
////        if(fetchedStudent!=null) {
////        	System.out.println("=== Fetched Student Information ===");
////            System.out.println("Roll No : " + fetchedStudent.getRollno());
////            System.out.println("Name    : " + fetchedStudent.getName());
////            System.out.println("Marks   : " + fetchedStudent.getMarks());
////            
////            System.out.println("=== Associated Laptops ===");
////            Collection<Laptop> lap = fetchedStudent.getLaptop();
////            for(Laptop l:lap) {
////            	System.out.println("Laptop ID: " + l.getLid() + " | Brand: " + l.getLname());
////            }
////         }else {
////        	 System.out.println("No Student found with rollno = 1");
////         }
//        session2.close();
//        
//        sf.close();
//
//        System.out.println("Updated successfully!");
//    }
//}