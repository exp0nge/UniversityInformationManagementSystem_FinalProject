import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_UILayer extends JFrame{
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
        frame.setSize(550, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);


        JPanel pane = new JPanel(new MigLayout());
        frame.getContentPane().add(pane);

        //CCNY banner
        JLabel bannerIcon = new JLabel();
        bannerIcon.setIcon(new ImageIcon("ccnyBanner2.png"));
        pane.add(bannerIcon, "span, wrap");


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
        searchTF.setMaximumSize(searchTF.getPreferredSize());
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
        JButton searchButton = new JButton("Go");
        searchButton.addActionListener(ae -> {
            searchButton.setEnabled(false); //turn off GO button
            if(searchTF.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please enter a name (case sensitive)");
                searchButton.setEnabled(true);
            }
            else {
                FacultyMode_BLLayer.searchForStudent(searchTF.getText(), searchResultsPanel);
                searchButton.setEnabled(true);
            }
        });

        searchComponent.setMinimumSize(minSizeUI);
        searchComponent.add(searchTF, "span 6, grow");
        searchComponent.add(searchButton, "wrap");
        searchComponent.add(searchResultsPanel, "span");
        tabbedPane.add(new JScrollPane(searchComponent), "Students");

        //Payroll Tab
        JComponent facultyPayroll = new JPanel(new MigLayout());

        //Add hours button stuff
        JButton addHours = new JButton("Add clocked hours");
        addHours.addActionListener(ae->{
            addNewHoursToPayroll(facultyPayroll, addHours);

        });
        facultyPayroll.add(addHours, "wrap");

        //Fetch previous inputted hours
        fetchPreviousClockedHours(facultyPayroll, addHours);
        tabbedPane.add(new JScrollPane(facultyPayroll), "Payroll");

        //Calender RSS feed
        JComponent calenderEventsPanel = new JPanel(new MigLayout());
        fetchCalenderEvents(calenderEventsPanel);
        tabbedPane.add(new JScrollPane(calenderEventsPanel), "Events (RSS)");

        //Message Center
        JComponent messagePanel = new JPanel(new MigLayout());
        JPanel messageBodyPanel = new JPanel(new MigLayout());

        JTextField messageSearchTF = new JTextField("Search for student...");
        messageSearchTF.setMinimumSize(messageSearchTF.getPreferredSize());
        messageSearchTF.setMaximumSize(messageSearchTF.getPreferredSize());
        messageSearchTF.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                messageSearchTF.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        JButton messageSearchButton = new JButton("Go");
        messageSearchButton.addActionListener(ae->{
            if(messageSearchTF.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Name is required to search!");
            }
            else {
                if(FacultyMode_BLLayer.searchForStudentToMessage(messageSearchTF.getText(), messageBodyPanel)){

                }
                else {
                    JOptionPane.showMessageDialog(null, "Student not found. Names must be spelled correctly.");
                }
            }
        });
        messagePanel.add(messageSearchTF, "span 6, grow");
        messagePanel.add(messageSearchButton, "wrap");
        messagePanel.add(messageBodyPanel, "span");

        tabbedPane.add(new JScrollPane(messagePanel), "Message Center");

        pane.add(helloNameLabel, "wrap");
        pane.add(tabbedPane, "span");

        frame.setVisible(true);

    }

    private void fetchCalenderEvents(JComponent calenderEventsPanel) {
        Map<String, String> listOfCalenderEvents = FacultyMode_BLLayer.getListOfCalenderEvents();
        for(Map.Entry<String, String> entry : listOfCalenderEvents.entrySet()){
            JLabel title = new JLabel(entry.getKey());
            String [] descriptElements = entry.getValue().split("!");
            JLabel time = new JLabel(descriptElements[0]);
            JLabel event = new JLabel(descriptElements[1]);

            calenderEventsPanel.add(title, "span, wrap");
            calenderEventsPanel.add(time, "span, wrap");
            calenderEventsPanel.add(event, "span, wrap");
            calenderEventsPanel.add(new JSeparator(), "growx, span, wrap");
        }
    }

    private void fetchPreviousClockedHours(JComponent facultyPayroll, JButton addHours) {
        facultyPayroll.removeAll();
        facultyPayroll.add(addHours, "wrap");
        List<String> hoursWorkedList = FacultyMode_BLLayer.getListOfWorkedHours();
        if(hoursWorkedList.size() > 0){
            for(String element : hoursWorkedList){
                JLabel clockedHours = new JLabel(element);
                facultyPayroll.add(clockedHours, "span, wrap");
                facultyPayroll.add(new JSeparator(), "span, growx, wrap");
            }
        }
    }

    private void addNewHoursToPayroll(JComponent facultyPayroll, JButton addHours) {
        JFrame miniFrame = new JFrame("Add hours");
        miniFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        miniFrame.setSize(200, 200);
        miniFrame.setResizable(false);
        miniFrame.setLocationRelativeTo(null);

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
            JOptionPane.showMessageDialog(null, "Hours added!");
            fetchPreviousClockedHours(facultyPayroll, addHours);
            miniFrame.dispatchEvent(new WindowEvent(miniFrame, WindowEvent.WINDOW_CLOSING));
        });
        panel.getRootPane().setDefaultButton(submitButton);

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
        classInfoFrame.setLocationRelativeTo(null);

        JPanel infoP = new JPanel(new MigLayout());
        classInfoFrame.getContentPane().add(infoP);
        JLabel classDeptL = new JLabel("Department: ");
        //Add department list to combobox
        String [] deptList = StudentMode_BLLayer.getListOfDepartments();
        JComboBox<String> deptListCBox = new JComboBox<>(deptList);

        JLabel classNameL = new JLabel("Class name: ");
        JComboBox<String> listOfCoursesCBox = new JComboBox<>(StudentMode_BLLayer.getListOfCourses("Computer Science"));

        deptListCBox.addActionListener(ae -> {
            String[] selCourseList = StudentMode_BLLayer.getListOfCourses((String) deptListCBox.getSelectedItem());
            listOfCoursesCBox.removeAllItems();
            for (int i = 0; i < selCourseList.length; i++) {
                listOfCoursesCBox.addItem(selCourseList[i]);
            }
        });



        JLabel gradeReceivedL = new JLabel("Grade received: ");
        JTextField gradeReceivedTF = new JTextField("N/A");

        JButton addClass = new JButton("Add class");
        addClass.addActionListener(ae ->{
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(FacultyMode_BLLayer.getST_filePath(), true);

                String newClassString = "\n" + "class:," + listOfCoursesCBox.getSelectedItem().toString() + ", " + deptListCBox.getSelectedItem().toString() + ", " + gradeReceivedTF.getText();
                fileOutputStream.write(newClassString.getBytes());
                FacultyMode_BLLayer.openStudentPanel(stUserName, jPane);
                classInfoFrame.dispatchEvent(new WindowEvent(classInfoFrame, WindowEvent.WINDOW_CLOSING));

            }
            catch (IOException e){
                e.printStackTrace();
            }
        });
        infoP.getRootPane().setDefaultButton(addClass);
        infoP.add(classDeptL);
        infoP.add(deptListCBox, "growx, push, wrap");
        infoP.add(classNameL);
        infoP.add(listOfCoursesCBox, "growx, push, wrap");
        infoP.add(gradeReceivedL);
        infoP.add(gradeReceivedTF, "growx, push, wrap");
        infoP.add(addClass);
        classInfoFrame.setVisible(true);
    }
    public static void loadContactInfoFrame(){
        //Show panel to display contact info:
        JFrame contactInfoFrame = new JFrame("Contact details");
        contactInfoFrame.setSize(380, 250);
        contactInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contactInfoFrame.setResizable(false);
        contactInfoFrame.setLocationRelativeTo(null);

        JPanel contactInfoP = new JPanel(new MigLayout());
        contactInfoFrame.getContentPane().add(contactInfoP);

        JButton editCell = new JButton("Edit Cell #");
        JButton editAddress = new JButton("Edit Address");
        JButton editeName = new JButton("Edit E-Contact Name");
        JButton editeNumber = new JButton("Edit E-Contact Number");

        //event listeners
        editCell.addActionListener(ae->{
            String cellNumber = JOptionPane.showInputDialog(null, "Enter a valid phone number (only digits)");
            if(!cellNumber.equals("")) {
                FacultyMode_BLLayer.contactInfo_setCellNumber(cellNumber);
                //reload the frame
                contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
                loadContactInfoFrame();
            }

        });
        editAddress.addActionListener(ae->{
            String address = JOptionPane.showInputDialog(null, "Enter a valid address (no commas)");
            if(!address.equals("")) {
                FacultyMode_BLLayer.contactInfo_setAddress(address);
                //reload frame
                contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
                loadContactInfoFrame();
            }
        });
        editeName.addActionListener(ae->{
            String emergencyName = JOptionPane.showInputDialog(null, "Enter a valid name of emergency contact");
            if(!emergencyName.equals("")) {
                FacultyMode_BLLayer.contactInfo_setEmergencyName(emergencyName);
                //reload frame
                contactInfoFrame.dispatchEvent(new WindowEvent(contactInfoFrame, WindowEvent.WINDOW_CLOSING));
                loadContactInfoFrame();
            }
        });
        editeNumber.addActionListener(ae->{
            String emergencyNumber = JOptionPane.showInputDialog(null, "Enter a valid phone number (only digits)");
            if(emergencyNumber.equals("")) {
                FacultyMode_BLLayer.contactInfo_setEmergencyNumber(emergencyNumber);
                //reload frame
                contactInfoFrame.dispose();
                loadContactInfoFrame();
            }
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
        frame.setSize(250, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new MigLayout());
        frame.getContentPane().add(panel);
        //iterate through to add JLabels for each scholarship
        for(int i = 0; i < listOfScholarships.size(); i ++){
            JLabel scholarship = new JLabel(listOfScholarships.get(i));
            panel.add(scholarship, "wrap");
        }

        JButton addScholarship = new JButton("Add scholarship");
        addScholarship.addActionListener(ae -> {
            String scholarshipName = JOptionPane.showInputDialog(null, "Enter name of scholarship:");
            if(scholarshipName.equals("")){
                JOptionPane.showMessageDialog(null, "Error, no scholarship name entered!");
            }
            else {
                FacultyMode_BLLayer.addScholarship(scholarshipName);
                JOptionPane.showMessageDialog(null, "New scholarship added");
                frame.dispose();
            }
        });

        panel.add(addScholarship);
        frame.setVisible(true);
    }


    public static void pushFoundStudentsForMessageButtons(List<String[]> listOfStudentsfound, JComponent messagePanel) {
        for(String [] element : listOfStudentsfound){
            JButton stButton = new JButton(element[0] + " " + element[1]);
            stButton.addActionListener(ae -> {
                openStudentMessageCenter(element[2], messagePanel);
            });
            messagePanel.add(stButton, "wrap");
            messagePanel.updateUI();
        }
    }

    private static void openStudentMessageCenter(String stName, JComponent messagePanel) {
        messagePanel.removeAll();
        messagePanel.updateUI();
        TitledBorder titledBorderMessageBody = new TitledBorder("Send a message");

        JLabel titleLabel = new JLabel("Title");
        JLabel messageLabel = new JLabel("Message");

        JTextField titleTF = new JTextField();
        titleTF.setMinimumSize(new Dimension(150, 20));
        JTextArea messageBodyTA = new JTextArea();
        messageBodyTA.setMinimumSize(new Dimension(420, 250));
        messageBodyTA.setMaximumSize(new Dimension(420, 250));
        messageBodyTA.setLineWrap(true);
        messageBodyTA.setWrapStyleWord(true);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(ae->{
            if(titleTF.getText().equals("") || messageBodyTA.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Enter a title and a message.");
            }
            messagePanel.setBorder(titledBorderMessageBody);
            if(FacultyMode_BLLayer.sendMessageToStudent(stName, titleTF.getText(), messageBodyTA.getText())){
                JOptionPane.showMessageDialog(null, "Message sent!");
                messagePanel.removeAll();
                messagePanel.updateUI();
                messagePanel.setBorder(null);
            }
        });

        messagePanel.add(titleLabel);
        messagePanel.add(titleTF, "span 25, wrap");
        messagePanel.add(messageLabel, "wrap");
        messagePanel.add(messageBodyTA, "grow, span 15 15, wrap");
        messagePanel.add(sendButton, "wrap");
    }


}
