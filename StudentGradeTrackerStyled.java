import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class GradeEntry {
    String subject;
    double grade;

    GradeEntry(String subject, double grade) {
        this.subject = subject;
        this.grade = grade;
    }

    String getGradeLetter() {
        if (grade >= 90) return "A";
        else if (grade >= 80) return "B";
        else if (grade >= 70) return "C";
        else if (grade >= 60) return "D";
        else return "F";
    }
}

class Student {
    String name;
    ArrayList<GradeEntry> grades = new ArrayList<>();

    Student(String name) {
        this.name = name;
    }

    void addGrade(String subject, double grade) {
        grades.add(new GradeEntry(subject, grade));
    }

    double getAverage() {
        if (grades.isEmpty()) return 0;
        double total = 0;
        for (GradeEntry g : grades) {
            total += g.grade;
        }
        return total / grades.size();
    }
}

public class StudentGradeTrackerStyled extends JFrame {
    private HashMap<String, Student> studentMap = new HashMap<>();
    private JTextField nameField, subjectField, gradeField;
    private JTextArea studentListArea;

    public StudentGradeTrackerStyled() {
        setTitle("Student Grade Tracker (With Grading Scale)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 520);
        setLocationRelativeTo(null);

        // Input Components
        nameField = new JTextField(15);
        subjectField = new JTextField(15);
        gradeField = new JTextField(5);

        JButton addButton = new JButton("Add Grade");
        JButton reportButton = new JButton("Show Report");

        studentListArea = new JTextArea(12, 40);
        studentListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(studentListArea);

        // Layout Setup
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(addButton);
        inputPanel.add(reportButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        addButton.addActionListener(e -> addGradeEntry());
        reportButton.addActionListener(e -> showStyledReport());
    }

    private void addGradeEntry() {
        String name = nameField.getText().trim();
        String subject = subjectField.getText().trim();
        String gradeText = gradeField.getText().trim();

        if (name.isEmpty() || subject.isEmpty() || gradeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double grade = Double.parseDouble(gradeText);
            Student student = studentMap.getOrDefault(name, new Student(name));
            student.addGrade(subject, grade);
            studentMap.put(name, student);

            studentListArea.append(name + " - " + subject + ": " + grade + "\n");

            nameField.setText("");
            subjectField.setText("");
            gradeField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid grade. Please enter a number.");
        }
    }

    private void showStyledReport() {
        if (studentMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student data available.");
            return;
        }

        double highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        String topStudent = "", topSubject = "", topGradeLetter = "";
        String lowStudent = "", lowSubject = "", lowGradeLetter = "";

        StringBuilder report = new StringBuilder("🎓 Student Grade Report\n");
        report.append("==================================\n");

        for (Student student : studentMap.values()) {
            report.append("\nStudent: ").append(student.name).append("\n");

            for (GradeEntry g : student.grades) {
                String gradeLetter = g.getGradeLetter();
                report.append("  • ").append(g.subject)
                      .append(" → Score: ").append(g.grade)
                      .append(" (").append(gradeLetter).append(")\n");

                if (g.grade > highest) {
                    highest = g.grade;
                    topStudent = student.name;
                    topSubject = g.subject;
                    topGradeLetter = gradeLetter;
                }

                if (g.grade < lowest) {
                    lowest = g.grade;
                    lowStudent = student.name;
                    lowSubject = g.subject;
                    lowGradeLetter = gradeLetter;
                }
            }

            report.append("  ➤ Average: ")
                  .append(String.format("%.2f", student.getAverage()))
                  .append("\n");
        }

        report.append("\n🏆 Highest Score: ").append(highest)
              .append(" (").append(topGradeLetter).append(") by ")
              .append(topStudent).append(" in ").append(topSubject).append("\n");

        report.append("🔻 Lowest Score: ").append(lowest)
              .append(" (").append(lowGradeLetter).append(") by ")
              .append(lowStudent).append(" in ").append(lowSubject).append("\n");

        JOptionPane.showMessageDialog(this, report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGradeTrackerStyled().setVisible(true);
        });
    }
}
