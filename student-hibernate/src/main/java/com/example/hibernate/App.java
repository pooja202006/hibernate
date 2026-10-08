package com.example.hibernate;

import com.example.hibernate.entity.Student;
import com.example.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Demonstrates: INSERT -> VERIFY -> UPDATE -> VERIFY using Hibernate + MySQL.
 */
public class App {

    public static void main(String[] args) {
        try {
            // 1. INSERT a new student
            Student student = new Student("Pavithra", "pavithra@example.com", "Computer Science");
            int id = saveStudent(student);
            System.out.println("\n[INSERT] Student saved with ID = " + id);

            // 2. VERIFY the insert by reading it back from the database
            Student inserted = getStudent(id);
            if (inserted != null) {
                System.out.println("[VERIFY INSERT] Record found in DB: " + inserted);
            } else {
                System.out.println("[VERIFY INSERT] FAILED - record not found!");
                return;
            }

            // 3. UPDATE the student's course and email
            updateStudent(id, "npavithra4046@gmail.com", "Artificial Intelligence and Data Science");
            System.out.println("[UPDATE] Student with ID " + id + " updated.");

            // 4. VERIFY the update
            Student updated = getStudent(id);
            System.out.println("[VERIFY UPDATE] Record in DB: " + updated);
            if (updated != null
                    && "Artificial Intelligence and Data Science".equals(updated.getCourse())
                    && "npavithra4046@gmail.com".equals(updated.getEmail())) {
                System.out.println("[VERIFY UPDATE] SUCCESS - changes persisted in the database.");
            } else {
                System.out.println("[VERIFY UPDATE] FAILED - values do not match.");
            }

            // 5. List all students in the table
            System.out.println("\nAll students in the table:");
            for (Student s : getAllStudents()) {
                System.out.println("  " + s);
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    /** Inserts a student and returns the generated ID. */
    public static int saveStudent(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(student);
            tx.commit();
            return student.getId();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /** Fetches a student by primary key (null if not found). */
    public static Student getStudent(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    /** Updates email and course of an existing student. */
    public static void updateStudent(int id, String newEmail, String newCourse) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student s = session.get(Student.class, id);
            if (s != null) {
                s.setEmail(newEmail);
                s.setCourse(newCourse);
                session.update(s);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /** Returns all students. */
    public static List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student", Student.class).list();
        }
    }
}
