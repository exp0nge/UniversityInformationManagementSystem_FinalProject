import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentMode_UILayer extends JFrame {
    private static String username;
    private static List<String []> listOfCourseInfo;
    private static StudentMode_UILayer inst; //Singleton
    private StudentMode_UILayer(){
        StudentMode_BLLayer.setStudentInfo();
        initComponents();
    }

    private void initComponents() {
        StudentMode_BLLayer.setStudentInfo();
        
        username = StudentMode_BLLayer.getUsername();
        JFrame frame = new JFrame("Student information for: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 430);
        frame.setResizable(false);

        JPanel panel = new JPanel(new MigLayout());
        frame.getContentPane().add(panel);

        //Hello "studentName" label
        JLabel helloStudent = new JLabel("Hello " +
                StudentMode_BLLayer.getStudentName() + ",");

        //CCNY banner icon
        JLabel ccnyBanner = new JLabel();
        ccnyBanner.setIcon(new ImageIcon("ccnyBanner.png"));

        //Create the tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        //Student info panel
        JComponent infoPanel = new JPanel(new MigLayout());
        JLabel idNumber = new JLabel("ID Number: " + Integer.toString(StudentMode_BLLayer.getStudentID()));
        JLabel scholarship = new JLabel("Scholarship: " + StudentMode_BLLayer.getScholarships());

        infoPanel.add(idNumber, "wrap");
        infoPanel.add(scholarship, "span, wrap");

        //E-contact info
        fetchContactInfo(infoPanel);

        //Classes tab
        JComponent coursesPanel = new JPanel(new MigLayout());
        TitledBorder coursePanelTitleBorder = BorderFactory.createTitledBorder("Course list");
        coursesPanel.setBorder(coursePanelTitleBorder);
        loadCourseInformation(coursesPanel);

        //Grades tab
        JComponent courseGradesPanel = new JPanel(new MigLayout());
        loadCourseGrades(courseGradesPanel);

        //Adding tabbedPane + helloStudent label
        tabbedPane.addTab("Student Info", infoPanel);
        tabbedPane.add(new JScrollPane(coursesPanel), "Courses");
        tabbedPane.add(new JScrollPane(courseGradesPanel), "Grades");


        panel.add(ccnyBanner, "span, wrap");
        panel.add(helloStudent, "wrap");
        panel.add(tabbedPane);

        frame.setVisible(true);
    }

    private void loadCourseInformation(JComponent coursesPanel) {
        if(listOfCourseInfo == null)
            listOfCourseInfo = StudentMode_BLLayer.getListOfCourseInfo();
        for(String [] element : listOfCourseInfo){
            JLabel courseTitle = new JLabel("Course: " + element[1]);
            JLabel courseDept = new JLabel("Department: " + element[2]);
            coursesPanel.add(courseTitle, "span, wrap");
            coursesPanel.add(courseDept, "span, wrap");
        }
    }
    private void loadCourseGrades(JComponent coursesPanel){
        if(listOfCourseInfo == null)
            listOfCourseInfo = StudentMode_BLLayer.getListOfCourseInfo();
        for(String [] element : listOfCourseInfo){
            JLabel courseTitle = new JLabel("Course: " + element[1]);
            JLabel courseGrade = new JLabel("Grade: " + element[3]);
            coursesPanel.add(courseTitle, "span, wrap");
            coursesPanel.add(courseGrade, "span, wrap");
        }

    }

    private void fetchContactInfo(JComponent infoPanel) {
        JPanel contactPanel = new JPanel(new MigLayout());
        TitledBorder contactPanelTitle = new TitledBorder("Contact Info");
        contactPanel.setBorder(contactPanelTitle);
        String [] eContactInfo = StudentMode_BLLayer.getEContactInfo();
        if(eContactInfo != null){
            JLabel phoneNumber = new JLabel("Phone number:" + eContactInfo[1]);
            JLabel address = new JLabel("Address:" + eContactInfo[2]);
            JLabel eContactName = new JLabel("Emergency contact name: " + eContactInfo[3]);
            JLabel eContactNumber = new JLabel("Emergency contact number:" + eContactInfo[4]);
            contactPanel.add(phoneNumber, "span, wrap");
            contactPanel.add(address, "span, wrap");
            contactPanel.add(eContactName, "span, wrap");
            contactPanel.add(eContactNumber, "span, wrap");
        }
        else {
            JLabel noContactsFound = new JLabel("No contact info found!");
            contactPanel.add(noContactsFound, "span, wrap");
        }
        infoPanel.add(contactPanel);
    }

    public static StudentMode_UILayer getInst(){
        if(inst == null)
            inst = new StudentMode_UILayer();

        return inst;
    }


}
