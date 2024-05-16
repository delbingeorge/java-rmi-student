import java.rmi.Remote;
import java.rmi.RemoteException;

interface MarkCalc extends Remote {
    double calculateTotal(double subject1, double subject2, double subject3) throws RemoteException;

    double calculateAverage(double subject1, double subject2, double subject3) throws RemoteException;

    String calculateGrade(double averageMarks) throws RemoteException;

    void saveToDatabase(String studentName, double subject1, double subject2, double subject3, double totalMarks,
            double averageMarks, String grade) throws RemoteException;

}
