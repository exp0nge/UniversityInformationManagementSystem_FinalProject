import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_BLLayer {
    private FacultyMode_BLLayer(){}
    public static String getName() {
        return FacultyMode_DALayer.getName();
    }

    public static void setUsername(String username) {
        FacultyMode_DALayer.setUsername(username);
    }

    public static void searchForStudent(String name, JPanel jPane) {
        if(FacultyMode_DALayer.searchForStudent(name)){
            JOptionPane.showMessageDialog(jPane, "Student found!");
            jPane.removeAll();
            jPane.updateUI();
            List<String []> listOfFoundStudents = FacultyMode_DALayer.getListOfStudentsFound();
            for(String [] element : listOfFoundStudents){
                JButton stButton = new JButton(element[0] + " " + element[1]);
                stButton.addActionListener(ae -> {
                    openStudentPanel(element[2], jPane);

                });
                jPane.add(stButton, "wrap");
                jPane.updateUI();
            }
        }
        else {
            JOptionPane.showMessageDialog(jPane, "Not found...");
        }
    }

    private static void openStudentPanel(String s, JPanel jPane) {
        jPane.removeAll();
        jPane.updateUI();
        FacultyMode_DALayer.openStudentPanel(s, jPane);
    }
}
