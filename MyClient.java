import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class MyClient extends JFrame {
    private JTextField studentNameField, subject1Field, subject2Field, subject3Field;
    private JTextArea resultArea;

    public MyClient() {
        createUI();
    }

    private void createUI() {
        setLayout(new GridLayout(6, 1));

        add(new JLabel("Student Name:"));
        studentNameField = new JTextField(10);
        add(studentNameField);

        add(new JLabel("Marks in Subject 1:"));
        subject1Field = new JTextField(10);
        add(subject1Field);

        add(new JLabel("Marks in Subject 2:"));
        subject2Field = new JTextField(10);
        add(subject2Field);

        add(new JLabel("Marks in Subject 3:"));
        subject3Field = new JTextField(10);
        add(subject3Field);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // connectToDatabase();
                calculate();
            }
        });
        add(calculateButton);

        resultArea = new JTextArea(10, 30);
        add(new JScrollPane(resultArea));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void calculate() {
        try {
            MarkCalc stub = (MarkCalc) Naming.lookup("rmi://localhost:5015/student");

            String studentName = studentNameField.getText();
            double subject1 = Double.parseDouble(subject1Field.getText());
            double subject2 = Double.parseDouble(subject2Field.getText());
            double subject3 = Double.parseDouble(subject3Field.getText());

            double totalMarks = stub.calculateTotal(subject1, subject2, subject3);
            double averageMarks = stub.calculateAverage(subject1, subject2, subject3);
            String grade = stub.calculateGrade(averageMarks);
            stub.saveToDatabase(studentName, subject1, subject2, subject3, totalMarks, averageMarks, grade);
            resultArea.setText("Student Name: " + studentName + "\n" + "Subject 1: " + subject1 + "\n"+ "Subject 2: " + subject2 + "\n"+ "Subject 3: " + subject3 + "\n"+ "Total Marks:" + totalMarks + "\n"+ "Average Marks: " + averageMarks + "\n"+ "Grade: " + grade + "\n");

        } catch (Exception e) {
            resultArea.setText(e.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyClient();
            }
        });
    }
}
