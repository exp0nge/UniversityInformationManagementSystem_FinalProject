import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);


        JPanel pane = new JPanel(new MigLayout());
        frame.getContentPane().add(pane);

        //Hello "user" label
        String helloString = "Hello " + FacultyMode_BLLayer.getName() + ",";
        JLabel helloNameLabel = new JLabel(helloString);

        //Jtabbed
        JTabbedPane tabbedPane = new JTabbedPane();
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
        JButton searchButton = new JButton("GO");
        searchButton.addActionListener(ae -> {
            searchButton.setEnabled(false); //turn off GO button
            FacultyMode_BLLayer.searchForStudent(searchTF.getText(), searchResultsPanel);
            searchButton.setEnabled(true);
        });
        searchComponent.setMinimumSize(minSizeUI);
        searchComponent.add(searchTF, "span 4");
        searchComponent.add(searchButton, "wrap");
        searchComponent.add(searchResultsPanel, "span");
        tabbedPane.addTab("Students", searchComponent);

        pane.add(helloNameLabel, "wrap");
        pane.add(tabbedPane, "span");

        frame.setVisible(true);

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

        });
        //addScholarships Functionality
        addScholarshipsButton.addActionListener(ae -> {

        });

        jPane.add(addClassButton, "wrap");
        jPane.add(addContactButton, "wrap");
        jPane.add(addScholarshipsButton, "wrap");

    }

    private static void newClassInfo(JPanel jPane, String stUserName) {
        //Show panel to display new class info:
        JFrame classInfoFrame = new JFrame();
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
        infoP.add(classNameL);
        infoP.add(classNameTF, "growx, push, wrap");
        infoP.add(classDeptL);
        infoP.add(classDeptTF, "growx, push, wrap");
        infoP.add(gradeReceivedL);
        infoP.add(gradeReceivedTF, "growx, push, wrap");
        infoP.add(addClass);
        classInfoFrame.setVisible(true);
    }
}
