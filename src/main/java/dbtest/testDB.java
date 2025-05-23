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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // CREATING RECORD TO A FILE
    public static void create(String name, int age, String section) {
        Connection conn = connect();
        String insertQuery = "INSERT INTO Student (studentName, studentAge, section) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, section);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int studentID = generatedKeys.getInt(1);
                Student student = new Student(studentID, name, age, section);
                Students.add(student);
            }
            System.out.println("Record inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    // Retrieving only ID
    public static void getStudentID() {
        Connection conn = connect();
        String query = "SELECT StudentID FROM Student";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("studentID");
                System.out.println(id);
            }
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
                String section = rs.getString("section");
                Student student = new Student(id, name, age, section);
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

    // Getting count
    public static void count() {
        Connection conn = connect();
        String query = "SELECT COUNT(Student.StudentID) as total,\n"
                + "       Student.section\n"
                + "  FROM Student\n"
                + "       JOIN\n"
                + "       Section ON Student.section = Section.section\n"
                + "   WHERE Student.section = \"LOYALTY\";";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt("total");
                System.out.println(count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
    public static void sum() {
        Connection conn = connect();
        String query = "SELECT SUM(StudentAge) as total FROM Student";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int sum = rs.getInt("total");
                System.out.println(sum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    // UPDATE A RECORD
    public static void update(int id, String newName, int newAge, String newSection) {
        Connection conn = connect();
        String updateQuery = "UPDATE Student SET studentName = ?, studentAge = ?, section = ? WHERE studentID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setString(3, newSection);
            pstmt.setInt(4, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student with ID " + id + " was updated successfully.");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

            Student updatedStudent = new Student(id, newName, newAge, newSection);
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

    // JOIN TABLE AND GET ATTRIBUTE OF OTHER TABLE
    public static void showJoinTable() {
        Connection conn = connect();
        String query = "SELECT Student.StudentID,\n"
                + "       Student.StudentName,\n"
                + "       Student.section,\n"
                + "       Section.teacherName\n"
                + "  FROM Student\n"
                + "       JOIN\n"
                + "       Section ON Student.section = Section.section;";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("studentID");
                String name = rs.getString("studentName");
                String section = rs.getString("section");
                String teacher = rs.getString("teacherName");

                System.out.println("ID: " + id
                        + " | Name: " + name
                        + " | Section: " + section
                        + " | Teacher: " + teacher);
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
