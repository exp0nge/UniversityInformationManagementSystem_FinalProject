import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_DALayer {
    private static List<String> accountInfoList;
    private static String username;
    private static Faculty facultyInst;
    private static List<String []> listOfStudentsFound;
    private static String [] listOfStudentsArray;

    private FacultyMode_DALayer(){}

    public static String getName() {
        return facultyInst.getName();
    }

    public static void setUsername(String usernameInput) {
        username = usernameInput;
        setEntityInformation();
    }

    public static void setEntityInformation(){
        accountInfoList = new ArrayList<String>();

        try{
            FileInputStream file = new FileInputStream("accounts/faculty/" + username + ".csv");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                accountInfoList.add(scanner.nextLine());
            }
            file.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        createInstanceOfFaculty();
    }

    private static void createInstanceOfFaculty() {
        String [] facultyInfo = accountInfoList.get(1).split(",");
        facultyInst = new Faculty(facultyInfo[0], facultyInfo[1], Integer.parseInt(facultyInfo[5].replace(" ", "")), "Computer Science");
    }

    public static boolean searchForStudent(String studentName){
        boolean state = false;
        try {
            FileInputStream studentDBfile = new FileInputStream("db.csv");
            Scanner scanner = new Scanner(studentDBfile);
            String nextLine;
            listOfStudentsFound = new ArrayList<>();
            while(scanner.hasNextLine()){
                nextLine = scanner.nextLine();
                listOfStudentsArray = nextLine.split(",");
                if(listOfStudentsArray.length > 3) {
                    //Search only the first name, last name, or ID
                    if (listOfStudentsArray[0].equals(studentName) || listOfStudentsArray[1].equals(studentName) || listOfStudentsArray[5].equals(studentName)) {
                        listOfStudentsFound.add(listOfStudentsArray);
                        state = true;
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return state;
    }

    public static List<String []> getListOfStudentsFound(){
        return listOfStudentsFound;
    }

    public static void openStudentPanel(String stUserName, JPanel jPane) {
        try{
            jPane.removeAll();
            jPane.updateUI();
            String filepath = "accounts/st/" + stUserName + ".csv";
            FileInputStream stFile = new FileInputStream(filepath);
            Scanner scanner = new Scanner(stFile);
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains("student")){
                    String [] basicInfoArray = line.split(",");
                    JLabel stName = new JLabel("Name: " + basicInfoArray[0] + " " + basicInfoArray[1] + ", ID: " + basicInfoArray[5]);
                    jPane.add(stName, "wrap");
                    //Add options for new class/contact/awards
                    openStudentClassOptionButtons(filepath, jPane, stUserName);
                }
                if(line.contains("class:")){
                    String [] classInfo = line.split(",");
                    String classInfoString = "Class: " + classInfo[1] + ", Department: " + classInfo[2] + ", Grade: " + classInfo[3];
                    JLabel classInfoJL = new JLabel(classInfoString);
                    JButton editGrade = new JButton("Modify Grade");
                    editGrade.addActionListener(ae -> {
                        String replacementGrade = JOptionPane.showInputDialog(jPane, "Input new grade: ");
                        modifyStGrade(filepath, classInfo, replacementGrade);
                        openStudentPanel(stUserName, jPane); //reset Jpanel
                    });
                    jPane.add(classInfoJL, "wrap");
                    jPane.add(editGrade, "wrap");
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void modifyStGrade(String stFile, String[] classInfo, String replacementGrade) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(stFile)));
            String s;
            String totalString = "";
            String oldClassString = "class:" + "," + classInfo[1] + "," + classInfo[2] + "," + classInfo[3];
            String newClassString = "class:" + "," + classInfo[1] + "," + classInfo[2] + ", " + replacementGrade;
            while((s = reader.readLine()) != null){
                totalString += s + "\n";
            }
            totalString = totalString.replace(oldClassString, newClassString);
            FileWriter fw = new FileWriter(new File(stFile));
            fw.write(totalString);
            fw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void openStudentClassOptionButtons(String filepath, JPanel jPane, String stUserName) {
        //Adding buttons: Add class, Add contact info, Add scholarships
        JButton addClassButton = new JButton("Add Class");
        JButton addContactButton = new JButton("Contact Info");
        JButton addScholarshipsButton = new JButton("Scholarships/Awards");

        //addClass Functionality
        addClassButton.addActionListener(ae -> {
            newClassInfo(filepath, jPane, stUserName);
        });
        //addContact Functionality
        addContactButton.addActionListener(ae -> {

        });
        //addScholarships Functionality
        addScholarshipsButton.addActionListener(ae -> {

        });

        jPane.add(addClassButton, "wrap");
        jPane.add(addContactButton, "wrap");
        jPane.add(addScholarshipsButton, "wrap");
    }

    private static void newClassInfo(String filepath, JPanel jPane, String username) {
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
                FileOutputStream fileOutputStream = new FileOutputStream(filepath, true);
                if (classDeptTF.getText().length() < 3 || classDeptTF.getText().length() < 2)
                {
                    JOptionPane.showMessageDialog(infoP, "Invalid input parameters");
                }
                else {
                    String newClassString = "\n" + "class:," + classNameTF.getText() + ", " + classDeptTF.getText() + ", " + gradeReceivedTF.getText();
                    fileOutputStream.write(newClassString.getBytes());
                    openStudentPanel(username, jPane);
                    classInfoFrame.dispatchEvent(new WindowEvent(classInfoFrame, WindowEvent.WINDOW_CLOSING));
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        });
        //TODO: Fix TF size issue
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
