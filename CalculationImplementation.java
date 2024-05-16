import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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

    public void saveToDatabase(String studentName, double subject1, double subject2, double subject3, double totalMarks,
            double averageMarks, String grade) throws RemoteException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root",
                "password")) {
            String query = "INSERT INTO student_reports (student_name, subject1, subject2, subject3,total_marks,average_marks,grade) VALUES (?, ?, ?, ?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentName);
            preparedStatement.setDouble(2, subject1);
            preparedStatement.setDouble(3, subject2);
            preparedStatement.setDouble(4, subject3);
            preparedStatement.setDouble(5, totalMarks);
            preparedStatement.setDouble(6, averageMarks);
            preparedStatement.setString(7, grade);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RemoteException("Database operation error", e);
        }
    }
}
