package dbtest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class testDB {

    static ArrayList<Student> Students = new ArrayList<>();

    // CONNECT TO DATABASE
    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:student.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // CREATING RECORD TO A FILE
    public static void create(String name, int age) {
        Connection conn = connect();
        String insertQuery = "INSERT INTO Student (studentName, studentAge) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int studentID = generatedKeys.getInt(1);
                Student student = new Student(studentID, name, age);
                Students.add(student);
            }
            System.out.println("Record inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    // READING ENTIRE TABLE
    public static void readTable() {
        Students.clear();
        Connection conn = connect();
        String selectQuery = "SELECT * FROM Student";

        try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("studentID");
                String name = rs.getString("studentName");
                int age = rs.getInt("studentAge");
                Student student = new Student(id, name, age);
                Students.add(student);
            }
            for (Student student : Students) {
                System.out.println(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    // UPDATE A RECORD
    public static void update(int id, String newName, int newAge) {
        Connection conn = connect();
        String updateQuery = "UPDATE Student SET studentName = ?, studentAge = ? WHERE studentID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student with ID " + id + " was updated successfully.");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

            Student updatedStudent = new Student(id, newName, newAge);
            Students.add(updatedStudent);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    // DELETE A RECORD
    public static void delete(int id) {
        Connection conn = connect();
        String deleteQuery = "DELETE FROM Student WHERE studentID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);
            Students.removeIf(student -> student.getStudentID() == id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student with ID " + id + " was deleted successfully");

            } else {
                System.out.println("Student with ID " + id + " was not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    // CLOSE CONNECTION TO PREVENT LEAKS
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
