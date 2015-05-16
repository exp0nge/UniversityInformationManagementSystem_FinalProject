import javax.swing.*;
import java.awt.*;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentMode_UILayer extends JFrame {
    private static String username;
    private static String studentInformationString;
    private static StudentMode_UILayer inst; //Singleton
    private StudentMode_UILayer(){
        studentInformationString = StudentFaculty_BLLayer.getStudentName();
        studentInformationString += "\n" + "ID Number: " + StudentFaculty_BLLayer.getStudentID();
        initComponents();
    }

    private static String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus rutrum eros sed convallis tincidunt. \nAliquas. Nullam vitae lectus nec mi tristique mollis ac non lacus. \nPhasellus id sapien et lorem dignissim consectetur sed ut ante. Duis a justo id nisi semper efficitur vel at massa. Maecenas semper finibus magna non finibus. ";


    private void initComponents() {
        StudentFaculty_BLLayer.setStudentInfo();
        
        username = StudentFaculty_BLLayer.getUsername();
        JFrame frame = new JFrame("Student information for: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;

        //Create the tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        //Student info panel
        JComponent infoPanel = new JPanel(new GridBagLayout());
        JTextArea infoText = new JTextArea(studentInformationString);
        infoText.setEditable(false);
        infoPanel.add(infoText);
        tabbedPane.addTab("Student Info", infoPanel);
        //Student courses
        JComponent stGrades = new JPanel(new GridBagLayout());
        stGrades.add(new JTextArea(lorem));
        tabbedPane.add("Grades", stGrades);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.BOTH;
        panel.add(tabbedPane, c);

        frame.setVisible(true);
    }

    public static StudentMode_UILayer getInst(){
        if(inst == null)
            inst = new StudentMode_UILayer();

        return inst;
    }


}
