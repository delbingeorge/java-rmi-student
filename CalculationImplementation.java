import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class CalculationImplementation extends UnicastRemoteObject implements MarkCalc {

    Connection connection;

    CalculationImplementation() throws RemoteException {

    }

    public double calculateTotal(double subject1, double subject2, double subject3) {
        return subject1 + subject2 + subject3;
    }

    public double calculateAverage(double subject1, double subject2, double subject3) {
        return (subject1 + subject2 + subject3) / 3.0;
    }

    public String calculateGrade(double averageMarks) {
        if (averageMarks >= 90) {
            return "A";
        } else if (averageMarks >= 80) {
            return "B";
        } else if (averageMarks >= 70) {
            return "C";
        } else if (averageMarks >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    public void saveToDatabase(String studentName, double regNo, double subject1, double subject2, double subject3,
            double totalMarks,
            double averageMarks, String grade) throws RemoteException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root",
                "")) {
            String query = "INSERT INTO student_reports (student_name, regNo, subject1, subject2, subject3,total_marks,average_marks,grade) VALUES (?, ?,?, ?, ?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentName);
            preparedStatement.setDouble(2, regNo);
            preparedStatement.setDouble(3, subject1);
            preparedStatement.setDouble(4, subject2);
            preparedStatement.setDouble(5, subject3);
            preparedStatement.setDouble(6, totalMarks);
            preparedStatement.setDouble(7, averageMarks);
            preparedStatement.setString(8, grade);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RemoteException("Database operation error", e);
        }
    }

    public String getStudentDetails(double registerNumber) throws RemoteException {
        String result = ""; // Initialize an empty string to hold the result
        String query = "SELECT * FROM student_reports WHERE regNo = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "");
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, registerNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String studentName = resultSet.getString("student_name");
                double subject1 = resultSet.getDouble("subject1");
                double subject2 = resultSet.getDouble("subject2");
                double subject3 = resultSet.getDouble("subject3");
                double totalMarks = resultSet.getDouble("total_marks");
                double averageMarks = resultSet.getDouble("average_marks");
                String grade = resultSet.getString("grade");

                // Format the result
                result = String.format(
                        "Student Name: %s%nRegistration Number: %.0f%nSubject 1: %.2f%nSubject 2: %.2f%nSubject 3: %.2f%nTotal Marks: %.2f%nAverage Marks: %.2f%nGrade: %s%n",
                        studentName, registerNumber, subject1, subject2, subject3, totalMarks, averageMarks, grade);
            } else {
                result = "Student not found.";
            }
        } catch (Exception e) {
            throw new RemoteException("Database operation error", e);
        }
        return result;
    }

}
