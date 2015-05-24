import javax.swing.*;
import java.io.*;
import java.util.*;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.Iterator;


/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_DALayer {
    private static List<String> accountInfoList;
    private static String username;
    private static Faculty facultyInst;
    private static List<String []> listOfStudentsFound;
    private static String [] listOfStudentsArray;
    private static String st_filepath;
    private static String[] st_contactInfoArray;
    private static List<String> st_scholarshipList;


    private FacultyMode_DALayer(){}

    public static String getName() {
        return facultyInst.getName();
    }

    public static void setUsername(String usernameInput) {
        username = usernameInput;
        setEntityInformation();
    }

    public static void setEntityInformation(){
        accountInfoList = new ArrayList<>();

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
                    if (listOfStudentsArray[0].equals(studentName) || listOfStudentsArray[1].equals(studentName) || listOfStudentsArray[5].equals(studentName) ||
                            (listOfStudentsArray[0] + " " + listOfStudentsArray[1]).equals(studentName) ||
                            listOfStudentsArray[0].toLowerCase().equals(studentName) ||
                            listOfStudentsArray[1].toLowerCase().equals(studentName) ||
                            (listOfStudentsArray[0].toLowerCase() + " " + listOfStudentsArray[1]).toLowerCase().equals(studentName)) {
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
            st_filepath = "accounts/st/" + stUserName + ".csv";
            FileInputStream stFile = new FileInputStream(st_filepath);
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
                    //openStudentClassOptionButtons(st_filepath, jPane, stUserName);
                }
                if(line.contains("class:")){
                    String [] classInfo = line.split(",");
                    String classInfoString = "Class: " + classInfo[1] + ", Department: " + classInfo[2] + ", Grade: " + classInfo[3];
                    JLabel classInfoJL = new JLabel(classInfoString);
                    JButton editGrade = new JButton("Modify Grade");
                    JButton deleteCourse = new JButton("Remove");
                    final String xfilePath = st_filepath;
                    final String courseToDeleteLine = line;
                    editGrade.addActionListener(ae -> {
                        String replacementGrade = JOptionPane.showInputDialog(jPane, "Input new grade: ");
                        modifyStGrade(xfilePath, classInfo, replacementGrade);
                        openStudentPanel(stUserName, jPane); //reset Jpanel
                    });
                    deleteCourse.addActionListener(ae->{
                        removeCourse(courseToDeleteLine);
                        openStudentPanel(stUserName, jPane); //reset panel
                    });
                    jPane.add(classInfoJL, "wrap");
                    jPane.add(editGrade);
                    jPane.add(deleteCourse, "wrap");
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void removeCourse(String classInfoString) {
        modifySTinfo(classInfoString, "\n");
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

    private static void modifySTinfo(String oldString, String newString){
        if(oldString == null || oldString.equals("")){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(st_filepath, true);
                fileOutputStream.write(newString.getBytes());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(st_filepath)));
                String s;
                String totalString = "";

                while ((s = reader.readLine()) != null) {
                    totalString += s + "\n";
                }
                totalString = totalString.replace(oldString, newString);
                FileWriter fw = new FileWriter(new File(st_filepath));
                fw.write(totalString);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void contactInfo() {

        st_contactInfoArray = new String[5];
        st_contactInfoArray[0] = "contact:,";
        st_contactInfoArray[1] = "null";
        st_contactInfoArray[2] = "null";
        st_contactInfoArray[3] = "null";
        st_contactInfoArray[4] = "null";

        //Check if info already available
        String contactInfo = searchForLineInFile("contact:");
        if(contactInfo != null){
            st_contactInfoArray = contactInfo.split(",");

        }
    }

    private static String searchForLineInFile(String searchString) {
        try{
            FileInputStream fileInputStream = new FileInputStream(st_filepath);
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
        return st_filepath;
    }

    public static String[] getSt_contactInfoArray() {
        return st_contactInfoArray;
    }

    public static void contactInfo_setCellNumber(String cellNumber) {
        st_contactInfoArray[1] = cellNumber;
        writeContactInfoToFile();
    }
    public static void contactInfo_setAddress(String address) {
        st_contactInfoArray[2] = address;
        writeContactInfoToFile();
    }

    public static void contactInfo_setEmergencyName(String emergencyName) {
        st_contactInfoArray[3] = emergencyName;
        writeContactInfoToFile();
    }

    public static void contactInfo_setEmergencyNumber(String emergencyNumber) {
        st_contactInfoArray[4] = emergencyNumber;
        writeContactInfoToFile();

    }

    private static void writeContactInfoToFile() {
        String checkIfExists = searchForLineInFile("contact:");
        String contactInfoString = "\n" + "contact:, " + st_contactInfoArray[1] + ", " +
                st_contactInfoArray[2] + ", " + st_contactInfoArray[3] + ", " +
                st_contactInfoArray[4];
        if(checkIfExists == null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(st_filepath, true);
                fileOutputStream.write(contactInfoString.getBytes());
                fileOutputStream.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            modifySTinfo(checkIfExists, contactInfoString);
        }
    }


    public static void scholarships() {
        if(st_scholarshipList == null)
            st_scholarshipList = new ArrayList<>();
        st_scholarshipList.clear(); //clear list
        try{
            FileInputStream fileInputStream = new FileInputStream(st_filepath);
            Scanner scanner = new Scanner(fileInputStream);
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains("scholarship")){
                    String [] tempArray = line.split(",");
                    st_scholarshipList.add(tempArray[1]);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static List<String> getSt_scholarshipList(){
        return st_scholarshipList;
    }

    public static void addScholarship(String scholarshipName) {
        String newScholarshipLine = "\n" + "scholarship:," + scholarshipName;
        writeScholarshipInfoToFile(newScholarshipLine);
    }

    private static void writeScholarshipInfoToFile(String newScholarshipLine) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(st_filepath, true);
            fileOutputStream.write(newScholarshipLine.getBytes());
            fileOutputStream.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void addNewHoursWorked(String startDayTFText, String endDayTFText, String hoursWorkedTFText) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("accounts/faculty/" + username + ".csv", true);
            String newHoursWorkedLine = "\n" + "hours:," + startDayTFText + "," + endDayTFText + ","
                    + hoursWorkedTFText;
            fileOutputStream.write(newHoursWorkedLine.getBytes());
            fileOutputStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<String> getListOfWorkedHours() {
        List<String> listOfHoursWorked = new ArrayList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream("accounts/faculty/" + username + ".csv");
            Scanner scanner = new Scanner(fileInputStream);
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains("hours")){
                    listOfHoursWorked.add(line);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return listOfHoursWorked;
    }

    public static Map<String, String> getListOfCalenderEvents() {
        Map<String, String> listOfCalenderEvents = new HashMap<>();
        try {
            URL url = new URL("http://www.ccny.cuny.edu/calendar/rss.cfm?xml=Calendar%20of%20Events%20RSS,RSS2.0");
            SyndFeedInput input = new SyndFeedInput();

            SyndFeed feed = input.build(new XmlReader(url));

            for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
            {
                SyndEntry entry = (SyndEntry) i.next();
                String descriptionOfEvent = entry.getPublishedDate() + "!" + entry.getDescription().getValue().replace("\n", "-");
                listOfCalenderEvents.put(entry.getTitle(), descriptionOfEvent);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return listOfCalenderEvents;
    }
}
