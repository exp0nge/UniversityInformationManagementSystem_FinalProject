import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_BLLayer {
    private static String ST_filePath;

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
            FacultyMode_UILayer.pushFoundStudentsButtons(listOfFoundStudents, jPane);
        }
        else {
            JOptionPane.showMessageDialog(jPane, "Not found...");
        }
    }

    public static void openStudentPanel(String s, JPanel jPane) {
        jPane.removeAll();
        jPane.updateUI();
        FacultyMode_DALayer.openStudentPanel(s, jPane);
    }


    public static void makeStudentNameJL(String textJL, JPanel jPane) {
        FacultyMode_UILayer.makeStudentNameJL(textJL, jPane);
    }

    public static void openStudentClassOptionButtons(JPanel jPane, String stUsername) {
        FacultyMode_UILayer.openStudentClassOptionsButtons(jPane, stUsername);
    }

    public static String getST_filePath() {
        ST_filePath = FacultyMode_DALayer.getST_filePath();
        return ST_filePath;
    }
}
