import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_UILayer extends JFrame {
    private static String username;
    private static FacultyMode_UILayer inst;
    private FacultyMode_UILayer(){
        username = LoginForm_BLLayer.getUsername();
        FacultyMode_BLLayer.setUsername(username);
        initComponents();
    }

    private void initComponents() {
        Dimension minSizeUI = new Dimension(500, 450);
        JFrame frame = new JFrame("Faculty mode");
        frame.setSize(540, 560);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);


        JPanel pane = new JPanel(new MigLayout());
        frame.getContentPane().add(pane);

        //Hello "user" label
        String helloString = "Hello " + FacultyMode_BLLayer.getName() + ",";
        JLabel helloNameLabel = new JLabel(helloString);

        //Jtabbed
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setMinimumSize(new Dimension(520, 480));
        JComponent searchComponent = new JPanel(new MigLayout());

        //Search tool
        JTextField searchTF = new JTextField("Search for student...");
        searchTF.setMinimumSize(searchTF.getPreferredSize());
        searchTF.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchTF.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        MigLayout searchPanelManager = new MigLayout(
                new LC().wrapAfter(6)
        );
        JPanel searchResultsPanel = new JPanel(searchPanelManager);
        //Go button
        JButton searchButton = new JButton("GO");
        searchButton.addActionListener(ae -> {
            searchButton.setEnabled(false); //turn off GO button
            FacultyMode_BLLayer.searchForStudent(searchTF.getText(), searchResultsPanel);
            searchButton.setEnabled(true);
        });
        pane.getRootPane().setDefaultButton(searchButton);
        searchComponent.setMinimumSize(minSizeUI);
        searchComponent.add(searchTF, "span 4");
        searchComponent.add(searchButton, "wrap");
        searchComponent.add(searchResultsPanel, "span");
        tabbedPane.add(new JScrollPane(searchComponent), "Students");

        //Payroll Tab
        JComponent facultyPayroll = new JPanel(new MigLayout());

        //Add hours button stuff
        JButton addHours = new JButton("Add clocked hours");
        addHours.addActionListener(ae->{
            addNewHoursToPayroll(facultyPayroll);

        });
        facultyPayroll.add(addHours, "wrap");

        //Fetch previous inputted hours
        fetchPreviousClockedHours(facultyPayroll);
        tabbedPane.add(new JScrollPane(facultyPayroll), "Payroll");


        pane.add(helloNameLabel, "wrap");
        pane.add(tabbedPane, "span");

        frame.setVisible(true);

    }

    private void fetchPreviousClockedHours(JComponent facultyPayroll) {
        List<String> hoursWorkedList = FacultyMode_BLLayer.getListOfWorkedHours();
        if(hoursWorkedList.size() > 0){
            for(String element : hoursWorkedList){
                JLabel clockedHours = new JLabel(element);
                facultyPayroll.add(clockedHours, "span, wrap");
            }
        }
    }

    private void addNewHoursToPayroll(JComponent facultyPayroll) {
        JFrame miniFrame = new JFrame("Add hours");
        miniFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        miniFrame.setSize(200, 250);
        JPanel panel = new JPanel(new MigLayout());
        miniFrame.getContentPane().add(panel);

        JLabel startDay = new JLabel("Start day:");
        JLabel endDay = new JLabel("End day:");
        JLabel hoursWorked = new JLabel("Hours worked:");

        JTextField startDayTF = new JTextField();
        JTextField endDayTF = new JTextField();
        JTextField hoursWorkedTF = new JTextField();

        //Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(ae->{
            FacultyMode_BLLayer.addNewHoursWorked(startDayTF.getText(), endDayTF.getText(), hoursWorkedTF.getText());
            JOptionPane.showMessageDialog(miniFrame, "Hours added!");
            fetchPreviousClockedHours(facultyPayroll);
            miniFrame.dispatchEvent(new WindowEvent(miniFrame, WindowEvent.WINDOW_CLOSING));
        });

        panel.add(startDay);
        panel.add(startDayTF, "push, growx, wrap");
        panel.add(endDay);
        panel.add(endDayTF, "push, growx, wrap");
        panel.add(hoursWorked);
        panel.add(hoursWorkedTF, "push, growx, wrap");
        panel.add(submitButton, "span");
        miniFrame.setVisible(true);
    }


    public static FacultyMode_UILayer getInst() {
        if(inst == null)
            inst = new FacultyMode_UILayer();
        return inst;
    }
    public static void pushFoundStudentsButtons(List<String []> listOfFoundStudents, JPanel jPane ){
        for(String [] element : listOfFoundStudents){
            JButton stButton = new JButton(element[0] + " " + element[1]);
            stButton.addActionListener(ae -> {
                FacultyMode_BLLayer.openStudentPanel(element[2], jPane);

            });
            jPane.add(stButton, "wrap");
            jPane.updateUI();
        }
    }


    public static void makeStudentNameJL(String textJL, JPanel jPane) {
        JLabel stName = new JLabel(textJL);
        jPane.add(stName, "wrap");
    }

    public static void openStudentClassOptionsButtons(JPanel jPane, String stUserName) {
        //Adding buttons: Add class, Add contact info, Add scholarships
        JButton addClassButton = new JButton("Add Class");
        JButton addContactButton = new JButton("Contact Info");
        JButton addScholarshipsButton = new JButton("Scholarships/Awards");

        //addClass Functionality
        addClassButton.addActionListener(ae -> {
            newClassInfo(jPane, stUserName);
        });
        //addContact Functionality
        addContactButton.addActionListener(ae -> {
            //FacultyMode_BLLayer.contactInfo(filepath);
            FacultyMode_BLLayer.contactInfo();

        });
        //addScholarships Functionality
        addScholarshipsButton.addActionListener(ae -> {
            FacultyMode_BLLayer.scholarships();
        });

        jPane.add(addClassButton, "wrap");
        jPane.add(addContactButton, "wrap");
        jPane.add(addScholarshipsButton, "wrap");

    }

    private static void newClassInfo(JPanel jPane, String stUserName) {
        //Show panel to display new class info:
        JFrame classInfoFrame = new JFrame("New class creation");
        classInfoFrame.setSize(380, 160);
        classInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        classInfoFrame.setResizable(false);
        JPanel infoP = new JPanel(new MigLayout());
        classInfoFrame.getContentPane().add(infoP);

        JLabel classNameL = new JLabel("Class name: ");
        JTextField classNameTF = new JTextField();
        JLabel classDeptL = new JLabel("Department: ");
        JTextField classDeptTF = new JTextField();
        JLabel gradeReceivedL = new JLabel("Grade received: ");
        JTextField gradeReceivedTF = new JTextField("N/A");

        JButton addClass = new JButton("Add class");
        addClass.addActionListener(ae ->{
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(FacultyMode_BLLayer.getST_filePath(), true);
                if (classDeptTF.getText().length() < 3 || classDeptTF.getText().length() < 2)
                {
                    JOptionPane.showMessageDialog(infoP, "Invalid input parameters");
                }
                else {
                    String newClassString = "\n" + "class:," + classNameTF.getText() + ", " + classDeptTF.getText() + ", " + gradeReceivedTF.getText();
                    fileOutputStream.write(newClassString.getBytes());
                    FacultyMode_BLLayer.openStudentPanel(stUserName, jPane);
                    classInfoFrame.dispatchEvent(new WindowEvent(classInfoFrame, WindowEvent.WINDOW_CLOSING));
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        });
        infoP.getRootPane().setDefaultButton(addClass);
        infoP.add(classNameL);
        infoP.add(classNameTF, "growx, push, wrap");
        infoP.add(classDeptL);
        infoP.add(classDeptTF, "growx, push, wrap");
        infoP.add(gradeReceivedL);
        infoP.add(gradeReceivedTF, "growx, push, wrap");
        infoP.add(addClass);
        classInfoFrame.setVisible(true);
    }
    public static void loadContactInfoFrame(){
        //Show panel to display contact info:
        JFrame contactInfoFrame = new JFrame();
        contactInfoFrame.setSize(380, 250);
        contactInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contactInfoFrame.setResizable(false);
        JPanel contactInfoP = new JPanel(new MigLayout());
        contactInfoFrame.getContentPane().add(contactInfoP);

        JButton editCell = new JButton("Edit Cell #");
        JButton editAddress = new JButton("Edit Address");
        JButton editeName = new JButton("Edit E-Contact Name");
        JButton editeNumber = new JButton("Edit E-Contact Number");

        //event listeners
        editCell.addActionListener(ae->{
            String cellNumber = JOptionPane.showInputDialog(contactInfoP, "Enter a valid phone number (only digits)");
            FacultyMode_BLLayer.contactInfo_setCellNumber(cellNumber);
            //reload the frame
            contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
            loadContactInfoFrame();

        });
        editAddress.addActionListener(ae->{
            String address = JOptionPane.showInputDialog(contactInfoP, "Enter a valid address (no commas)");
            FacultyMode_BLLayer.contactInfo_setAddress(address);
            //reload frame
            contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
            loadContactInfoFrame();
        });
        editeName.addActionListener(ae->{
            String emergencyName = JOptionPane.showInputDialog(contactInfoP, "Enter a valid name of emergency contact");
            FacultyMode_BLLayer.contactInfo_setEmergencyName(emergencyName);
            //reload frame
            contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
            loadContactInfoFrame();
        });
        editeNumber.addActionListener(ae->{
            String emergencyNumber = JOptionPane.showInputDialog(contactInfoP, "Enter a valid phone number (only digits)");
            FacultyMode_BLLayer.contactInfo_setEmergencyNumber(emergencyNumber);
            //reload frame
            contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
            loadContactInfoFrame();
        });



        //push them to panel
        String [] contactInfoArray = FacultyMode_BLLayer.getOldContactInfo();
        oldContactInfoExits(contactInfoP, contactInfoArray);
        contactInfoP.add(editCell, "wrap");
        contactInfoP.add(editAddress, "wrap");
        contactInfoP.add(editeName, "wrap");
        contactInfoP.add(editeNumber);
        contactInfoFrame.setVisible(true);
    }
    public static void oldContactInfoExits(JPanel contactInfoP, String contactInfoArray[]){
        JLabel cellNubmerL = new JLabel("Cell #: " + contactInfoArray[1]);
        JLabel addressL = new JLabel("Address: " + contactInfoArray[2]);
        JLabel eName = new JLabel("Emergency Contact: " + contactInfoArray[3]);
        JLabel eNumber = new JLabel("Emergency Number: " + contactInfoArray[4]);
        contactInfoP.add(cellNubmerL, "wrap");
        contactInfoP.add(addressL, "wrap");
        contactInfoP.add(eName, "wrap");
        contactInfoP.add(eNumber, "wrap");
    }

    public static void loadScholarships(List<String> listOfScholarships) {
        JFrame frame = new JFrame("Scholarships");
        frame.setSize(200, 400);
        frame.setResizable(false);

        JPanel panel = new JPanel(new MigLayout());
        frame.getContentPane().add(panel);
        //iterate through to add JLabels for each scholarship
        for(int i = 0; i < listOfScholarships.size(); i ++){
            JLabel scholarship = new JLabel(listOfScholarships.get(i));
            panel.add(scholarship, "wrap");
        }

        JButton addScholarship = new JButton("Add scholarship");
        addScholarship.addActionListener(ae -> {
            String scholarshipName = JOptionPane.showInputDialog(panel, "Enter name of scholarship:");
            FacultyMode_BLLayer.addScholarship(scholarshipName);
            JOptionPane.showMessageDialog(panel, "New scholarship added");
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        panel.add(addScholarship);
        frame.setVisible(true);
    }
}
