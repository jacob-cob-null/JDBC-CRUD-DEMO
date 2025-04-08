package dbtest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class testDB {

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:student.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn; // Return the connection object
    }

    //CREATING RECORD TO A FILE
    public static void create(int id, String name, int age) {
        Connection conn = connect();
        String insertQuery = "INSERT INTO Student (studentID, studentName, studentAge) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
    public static void update(int id, String newName, int newAge){
        Connection conn = connect();
        String updateQuery = "UPDATE Student SET studentName = ?, studentAge = ? WHERE studentID = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student with ID " + id + " was updated successfully.");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    //READING ENTIRE TABLE
    public static void readTable() {
        Connection conn = connect();
        String selectQuery = "SELECT * FROM Student";

        try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("studentID");
                String name = rs.getString("studentName");
                int age = rs.getInt("studentAge");
                System.out.println("ID:" + id);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age + "/n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
    //DELETE A RECORD
    public static void delete(int id) {
        Connection conn = connect();
        String deleteQuery = "DELETE FROM Student WHERE studentID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student with ID " + id + " was deleted successfully");
            } else {
                System.out.println("Student with ID" + id + " was not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
    //CLOSE CONNECTION TO PREVENT LEAKS
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
