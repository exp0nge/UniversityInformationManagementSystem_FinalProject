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

    public static void contactInfo() {
        FacultyMode_DALayer.contactInfo();
        FacultyMode_UILayer.loadContactInfoFrame();

    }
    public static String[] getOldContactInfo(){
        return FacultyMode_DALayer.getSt_contactInfoArray();
    }

    public static void contactInfo_setCellNumber(String cellNumber) {
        FacultyMode_DALayer.contactInfo_setCellNumber(cellNumber);
    }

    public static void contactInfo_setAddress(String address) {
        FacultyMode_DALayer.contactInfo_setAddress(address);
    }

    public static void contactInfo_setEmergencyName(String emergencyName) {
        FacultyMode_DALayer.contactInfo_setEmergencyName(emergencyName);
    }

    public static void contactInfo_setEmergencyNumber(String emergencyNumber) {
        FacultyMode_DALayer.contactInfo_setEmergencyNumber(emergencyNumber);
    }

    public static void scholarships() {
        FacultyMode_DALayer.scholarships();
        FacultyMode_UILayer.loadScholarships(getListOfScolarships());
    }

    public static List<String> getListOfScolarships(){
        return FacultyMode_DALayer.getSt_scholarshipList();
    }

    public static void addScholarship(String scholarshipName) {
        FacultyMode_DALayer.addScholarship(scholarshipName);
    }

    public static void addNewHoursWorked(String startDayTFText, String endDayTFText, String hoursWorkedTFText) {
        FacultyMode_DALayer.addNewHoursWorked(startDayTFText, endDayTFText, hoursWorkedTFText);
    }

    public static List<String> getListOfWorkedHours() {
        List<String> listOfHoursWorked = FacultyMode_DALayer.getListOfWorkedHours();
        String line;
        for(int i = 0; i < listOfHoursWorked.size(); i++){
            line = listOfHoursWorked.get(i);
            line = line.replace("hours:", "Clocked in event:");
            line = line.replace(",", " ");
            listOfHoursWorked.set(i, line);
        }
        return listOfHoursWorked;
    }
}
