import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class MyClient extends JFrame {
    private JTextField studentNameField, subject1Field, subject2Field, subject3Field, stdRegNumber, displayReg;
    private JTextArea resultArea;
    JOptionPane pane = new JOptionPane();

    public MyClient() {
        createUI();
    }

    private void createUI() {
        setLayout(new GridLayout(6, 1));

        addLabelAndTextField("Reg Number:", stdRegNumber = new JTextField(4));
        addLabelAndTextField("Student Name:", studentNameField = new JTextField(4));
        addLabelAndTextField("Marks in Subject 1:", subject1Field = new JTextField(4));
        addLabelAndTextField("Marks in Subject 2:", subject2Field = new JTextField(4));
        addLabelAndTextField("Marks in Subject 3:", subject3Field = new JTextField(4));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
        add(calculateButton);

        addLabelAndTextField("Enter register number to display:", displayReg = new JTextField(4));
        JButton displayButton = new JButton("Display");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStdDetails();
            }
        });
        add(displayButton);

        resultArea = new JTextArea(10, 30);
        add(new JScrollPane(resultArea));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addLabelAndTextField(String labelText, JTextField textField) {
        add(new JLabel(labelText));
        add(textField);
    }

    private void displayStdDetails() {
        try {
            MarkCalc stub = (MarkCalc) Naming.lookup("rmi://localhost:5015/student");

            double registerNumber = Double.parseDouble(displayReg.getText());

            // Call the method on the server to get the student details
            String studentDetails = stub.getStudentDetails(registerNumber);
            System.out.println(studentDetails);
            // Display the student details in a JOptionPane dialog box
            JOptionPane.showMessageDialog(this, studentDetails, "Student Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            // Display any exceptions in a JOptionPane error dialog box
            JOptionPane.showMessageDialog(this, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculate() {
        try {
            MarkCalc stub = (MarkCalc) Naming.lookup("rmi://localhost:5015/student");

            double regNo = Double.parseDouble(stdRegNumber.getText());
            String studentName = studentNameField.getText();
            double subject1 = Double.parseDouble(subject1Field.getText());
            double subject2 = Double.parseDouble(subject2Field.getText());
            double subject3 = Double.parseDouble(subject3Field.getText());

            double totalMarks = stub.calculateTotal(subject1, subject2, subject3);
            double averageMarks = stub.calculateAverage(subject1, subject2, subject3);
            String grade = stub.calculateGrade(averageMarks);

            stub.saveToDatabase(studentName, regNo, subject1, subject2, subject3, totalMarks, averageMarks, grade);
            resultArea.setText("Added to database!");

            // resultArea.setText("Reg No:" + regNo + "Student Name: " + studentName + "\n"
            // + "Subject 1: " + subject1
            // + "\n" + "Subject 2: "
            // + subject2 + "\n" + "Subject 3: " + subject3 + "\n" + "Total Marks:" +
            // totalMarks + "\n"
            // + "Average Marks: " + averageMarks + "\n" + "Grade: " + grade + "\n");

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
