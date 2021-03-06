import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentMode_UILayer extends JFrame {
    private static String username;
    private static List<String []> listOfCourseInfo;
    private static StudentMode_UILayer inst; //Singleton
    private final String firstItemInDepartments = "Computer Science";

    private StudentMode_UILayer(){
        StudentMode_BLLayer.setStudentInfo();
        initComponents();
    }

    private void initComponents() {
        StudentMode_BLLayer.setStudentInfo();
        
        username = StudentMode_BLLayer.getUsername();
        JFrame frame = new JFrame("Student information for: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 450);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

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

        //Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(ae->{
            int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
            if(response == JOptionPane.YES_OPTION)
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        infoPanel.add(logoutButton);

        //Classes tab
        JComponent coursesPanel = new JPanel(new MigLayout());
        TitledBorder coursePanelTitleBorder = BorderFactory.createTitledBorder("Course list");
        coursesPanel.setBorder(coursePanelTitleBorder);
        loadCourseInformation(coursesPanel);

        //Grades tab
        JComponent courseGradesPanel = new JPanel(new MigLayout());
        loadCourseGrades(courseGradesPanel);

        //Make schedule
        JComponent planSchedulePanel = new JPanel(new MigLayout());

        //make combobox of departments
        JComboBox<String> deptComboBox = departmentComboBox(planSchedulePanel);
        JComboBox<String> coursesOfferedInDept = new JComboBox<>(StudentMode_BLLayer.getListOfCourses(firstItemInDepartments));
        planSchedulePanel.add(coursesOfferedInDept, "wrap");

        deptComboBox.addActionListener(ae->{
            String [] selCourseList = StudentMode_BLLayer.getListOfCourses((String)deptComboBox.getSelectedItem());
            addToCourseComboBox(coursesOfferedInDept, selCourseList);
        });

        //View previously added and add buttons
        JButton viewPreviousCoursesButton = new JButton("View saved");
        JButton saveCourseButton = new JButton("Save course");
        viewPreviousCoursesButton.addActionListener(ae->{
            viewSavedCourses(courseGradesPanel);
        });
        saveCourseButton.addActionListener(ae->{
            saveSelectedCourse(courseGradesPanel, deptComboBox, coursesOfferedInDept);
        });
        planSchedulePanel.add(viewPreviousCoursesButton);
        planSchedulePanel.add(saveCourseButton);

        //Events RSS feed
        JComponent eventsRSS = new JPanel(new MigLayout());
        fetchRSSEvents(eventsRSS);

        //Adding tabbedPane + helloStudent label
        tabbedPane.addTab("Summary", infoPanel);
        tabbedPane.add(new JScrollPane(coursesPanel), "Courses");
        tabbedPane.add(new JScrollPane(courseGradesPanel), "Grades");
        tabbedPane.add(new JScrollPane(planSchedulePanel), "Planner");
        tabbedPane.add(new JScrollPane(eventsRSS), "Events");

        panel.add(ccnyBanner, "span, wrap");
        panel.add(helloStudent, "wrap");
        panel.add(tabbedPane);
        frame.setVisible(true);

        //check for messages
        StudentMode_BLLayer.checkForMessages();

    }

    private void fetchRSSEvents(JComponent eventsRSS) {
        Map<String, String> listOfCalenderEvents = FacultyMode_BLLayer.getListOfCalenderEvents();
        for(Map.Entry<String, String> entry : listOfCalenderEvents.entrySet()){
            JLabel title = new JLabel(entry.getKey());
            String [] descriptElements = entry.getValue().split("!");
            JLabel time = new JLabel(descriptElements[0]);
            JLabel event = new JLabel(descriptElements[1]);

            eventsRSS.add(title, "span, wrap");
            eventsRSS.add(time, "span, wrap");
            eventsRSS.add(event, "span, wrap");
            eventsRSS.add(new JSeparator(), "growx, span, wrap");
        }
    }

    private void viewSavedCourses(JComponent courseGradesPanel) {
        JFrame savedCourseFrame = new JFrame("Saved courses");
        savedCourseFrame.setDefaultCloseOperation(savedCourseFrame.DISPOSE_ON_CLOSE);
        savedCourseFrame.setSize(325, 300);
        savedCourseFrame.setResizable(false);
        savedCourseFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new MigLayout());
        savedCourseFrame.getContentPane().add(new JScrollPane(panel));
        //Button to clear saved courses
        JButton clearSavedButton = new JButton("Clear list");
        clearSavedButton.addActionListener(ae->{
            int response = JOptionPane.showConfirmDialog(null, "***WARNING***\nAre you sure you want to delete?\n***NO WAY TO UNDO****",
                    "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
            if(response == JOptionPane.YES_OPTION) {
                StudentMode_BLLayer.clearSavedCourses();
                savedCourseFrame.dispatchEvent(new WindowEvent(savedCourseFrame, WindowEvent.WINDOW_CLOSING));
            }
        });
        panel.add(clearSavedButton, "wrap");

        MigLayout listOfCoursesLayoutMang = new MigLayout(
                new LC().wrapAfter(2)
        );
        JPanel listOfCourses = new JPanel(listOfCoursesLayoutMang);
        TitledBorder listOfCoursesTB = new TitledBorder("Course list");
        listOfCourses.setBorder(listOfCoursesTB);
        String [] savedCourses = StudentMode_BLLayer.getArraySavedCourses();
        if(savedCourses != null){
            for(int i = 1; i < savedCourses.length; i++){
                JLabel courseInfo = new JLabel(savedCourses[i]);
                listOfCourses.add(courseInfo);
            }
        }
        else{
            JLabel noSavedCoursesLabel = new JLabel("No courses saved.");
            listOfCourses.add(noSavedCoursesLabel);
        }
        panel.add(listOfCourses);
        savedCourseFrame.setVisible(true);

    }

    private void saveSelectedCourse(JComponent courseGradesPanel, JComboBox<String> deptComboBox, JComboBox<String> coursesOfferedInDept) {
        if(StudentMode_BLLayer.savePlannedCourse((String)deptComboBox.getSelectedItem(), (String)coursesOfferedInDept.getSelectedItem())){

        }
        else{
            JOptionPane.showMessageDialog(null, "Error, course already exists in cart.");
        }
    }

    private void addToCourseComboBox(JComboBox<String> coursesOfferedInDept, String[] selCourseList) {
        coursesOfferedInDept.removeAllItems();
        for(int i = 0; i < selCourseList.length; i++){
            coursesOfferedInDept.addItem(selCourseList[i]);
        }
    }

    private JComboBox<String> departmentComboBox(JComponent planSchedulePanel) {
        String [] deptList = StudentMode_BLLayer.getListOfDepartments();
        JComboBox<String> deptListBox = new JComboBox<>(deptList);
        planSchedulePanel.add(deptListBox);
        return deptListBox;
    }

    private void loadCourseInformation(JComponent coursesPanel) {
        if(listOfCourseInfo == null)
            listOfCourseInfo = StudentMode_BLLayer.getListOfCourseInfo();
        for(String [] element : listOfCourseInfo){
            JLabel courseTitle = new JLabel("Course: " + element[1]);
            JLabel courseDept = new JLabel("Department: " + element[2]);
            coursesPanel.add(courseTitle, "span, wrap");
            coursesPanel.add(courseDept, "span, wrap");
            coursesPanel.add(new JSeparator(), "span, growx, wrap");
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
            coursesPanel.add(new JSeparator(), "span, growx, wrap");
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
        infoPanel.add(contactPanel, "wrap");
    }

    public static StudentMode_UILayer getInst(){
        if(inst == null)
            inst = new StudentMode_UILayer();

        return inst;
    }

    public static void showMessages(List<String[]> messageList) {
        JFrame messageFrame = new JFrame("Messages");
        messageFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        messageFrame.setSize(350, 250);
        messageFrame.setResizable(false);
        messageFrame.setLocationRelativeTo(null);

        JPanel messagePanel = new JPanel(new MigLayout());
        //JScrollPane scrollPane = new JScrollPane();
        //scrollPane.setViewportView(messagePanel);
        messageFrame.getContentPane().add(new JScrollPane(messagePanel));

        loadMessagesToPanel(messageList, messagePanel, messageFrame);

        messageFrame.setVisible(true);
    }

    private static void loadMessagesToPanel(List<String[]> messageList, JPanel messagePanel, JFrame frame) {
        messagePanel.removeAll();
        messagePanel.updateUI();
        for(String[] messageItem : messageList){
            JLabel senderLabel = new JLabel("Sender: " + messageItem[1]);
            JLabel titleLabel = new JLabel("Title: " + messageItem[2]);

            JTextArea messageBodyTA = new JTextArea(2, 25);
            messageBodyTA.setText(messageItem[3]);
            messageBodyTA.setWrapStyleWord(true);
            messageBodyTA.setLineWrap(true);
            messageBodyTA.setEditable(false);
            messageBodyTA.setFocusable(false);
            messageBodyTA.setOpaque(false);
            messageBodyTA.setBackground(UIManager.getColor("Label.background"));
            messageBodyTA.setBorder(UIManager.getBorder("Label.border"));

            JButton deleteMessageButton = new JButton("Delete Message");
            deleteMessageButton.addActionListener(ae->{
                int response = JOptionPane.showConfirmDialog(messagePanel, "Mark for deletion?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                if(response == JOptionPane.YES_OPTION) {
                    String fullLineMessage = messageItem[0] + "," + messageItem[1] + "," + messageItem[2] + "," + messageItem[3];
                    StudentMode_BLLayer.deleteMessage(fullLineMessage);
                    //reload panel
                    frame.dispose();
                    StudentMode_BLLayer.checkForMessages();
                }
            });

            messagePanel.add(senderLabel, "wrap");
            messagePanel.add(titleLabel, "wrap");
            messagePanel.add(messageBodyTA, "span, growx, wrap");
            messagePanel.add(deleteMessageButton);
            messagePanel.add(new JSeparator(), "growx, span, wrap");
        }
    }
}
