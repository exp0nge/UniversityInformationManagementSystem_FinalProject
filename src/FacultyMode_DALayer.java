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
    private static String filepath;

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
            filepath = "accounts/st/" + stUserName + ".csv";
            FileInputStream stFile = new FileInputStream(filepath);
            Scanner scanner = new Scanner(stFile);
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains("student")){
                    String [] basicInfoArray = line.split(",");
                    String stNameLabel = "Name: " + basicInfoArray[0] + " " + basicInfoArray[1] + ", ID: " + basicInfoArray[5];
                    FacultyMode_BLLayer.makeStudentNameJL(stNameLabel, jPane);
                    //Add options for new class/contact/awards
                    FacultyMode_BLLayer.openStudentClassOptionButtons(jPane, stUserName);
                    //openStudentClassOptionButtons(filepath, jPane, stUserName);
                }
                if(line.contains("class:")){
                    String [] classInfo = line.split(",");
                    String classInfoString = "Class: " + classInfo[1] + ", Department: " + classInfo[2] + ", Grade: " + classInfo[3];
                    JLabel classInfoJL = new JLabel(classInfoString);
                    JButton editGrade = new JButton("Modify Grade");
                    final String xfilePath = filepath;
                    editGrade.addActionListener(ae -> {
                        String replacementGrade = JOptionPane.showInputDialog(jPane, "Input new grade: ");
                        modifyStGrade(xfilePath, classInfo, replacementGrade);
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

    private static void modifySTinfo(String stFile, String oldString, String newString){
        if(oldString == null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(stFile, true);
                fileOutputStream.write(newString.getBytes());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(stFile)));
                String s;
                String totalString = "";

                while ((s = reader.readLine()) != null) {
                    totalString += s + "\n";
                }
                totalString = totalString.replace(oldString, newString);
                FileWriter fw = new FileWriter(new File(stFile));
                fw.write(totalString);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private static void contactInfo(String filepath) {
        //Show panel to display contact info:
        JFrame contactInfoFrame = new JFrame();
        contactInfoFrame.setSize(380, 250);
        contactInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contactInfoFrame.setResizable(false);
        JPanel contactInfoP = new JPanel(new MigLayout());
        contactInfoFrame.getContentPane().add(contactInfoP);
        //Check if info already available
        String contactInfo = searchForLineInFile(filepath, "contact:");
        if(contactInfo != null){
            String [] contactInfoArray = contactInfo.split(",");
            JLabel cellNubmerL = new JLabel("Cell #: " + contactInfoArray[1]);
            JLabel addressL = new JLabel("Address: " + contactInfoArray[2]);
            JLabel eName = new JLabel("Emergency Contact: " + contactInfoArray[3]);
            JLabel eNumber = new JLabel("Emergency Number: " + contactInfoArray[4]);
            contactInfoP.add(cellNubmerL, "wrap");
            contactInfoP.add(addressL, "wrap");
            contactInfoP.add(eName, "wrap");
            contactInfoP.add(eNumber, "wrap");
        }
        JButton editCell = new JButton("Edit Cell #");
        JButton editAddress = new JButton("Edit Address");
        JButton editeName = new JButton("Edit E-Contact Name");
        JButton editeNumber = new JButton("Edit E-Contact Number");



        contactInfoP.add(editCell, "wrap");
        contactInfoP.add(editAddress, "wrap");
        contactInfoP.add(editeName, "wrap");
        contactInfoP.add(editeNumber);
        contactInfoFrame.setVisible(true);
    }

    private static String searchForLineInFile(String filepath, String searchString) {
        try{
            FileInputStream fileInputStream = new FileInputStream(filepath);
            Scanner scanner = new Scanner(fileInputStream);
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains(searchString)){
                    return line;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getST_filePath() {
        return filepath;
    }
}
