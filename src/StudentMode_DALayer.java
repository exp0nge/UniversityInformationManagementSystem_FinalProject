import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentMode_DALayer {
    private static String username;
    private static String filepath;
    private static List<String> accountInfoList;
    private static Student student;
    private static List<String[]> listOfCourseInfo;

    private StudentMode_DALayer(){

    }

    public static void setUsername(String userName) {
        username = userName;
        filepath = "accounts/st/" + username + ".csv";
        setEntityInformation();
        listOfCourseInfo = new ArrayList<>();
    }
    public static void setEntityInformation(){
        accountInfoList = new ArrayList<String>();

        try{
            FileInputStream file = new FileInputStream("accounts/st/" + username + ".csv");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                accountInfoList.add(scanner.nextLine());
            }
            file.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        createInstanceOfStudent();
    }

    private static void createInstanceOfStudent() {
        String [] stInfoArray = accountInfoList.get(1).split(",");
        student = new Student(stInfoArray[0], stInfoArray[1], 4.0, Integer.parseInt(stInfoArray[5].replace(" ", "")));
    }

    protected static String getName(){
        return student.getName();
    }

    protected static int getID(){
        return student.getID();
    }

    public static List<String> getAccountInfoList() {
        return accountInfoList;
    }

    public static String getScholarships() {
        String scholarships = searchForLine("scholarships");
        if(scholarships != null){
            scholarships = scholarships.replace("scholarships:", "");
            scholarships = scholarships.replace(",", "");
            return scholarships;
        }
        return "None";
    }

    private static String searchForLine(String line){
        try{
            FileInputStream fileInputStream = new FileInputStream(filepath);
            Scanner scanner = new Scanner(fileInputStream);
            String searcher;
            while(scanner.hasNextLine()){
                searcher = scanner.nextLine();
                if(searcher.contains(line)){
                    fileInputStream.close();
                    return searcher;
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getEContactInfo() {
        String contactLine = searchForLine("contact");
        if(contactLine != null) {
            contactLine = contactLine.replace("contact:", "");
            String[] contactArray = contactLine.split(",");
            return contactArray;
        }
        else
            return null;
    }

    public static List<String []> getListOfCourseInfo() {
        searchForMultiLine("class");
        return listOfCourseInfo;
    }

    private static void searchForMultiLine(String aClass) {
        listOfCourseInfo = new ArrayList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(filepath);
            Scanner scanner = new Scanner(fileInputStream);
            String searcher;
            while(scanner.hasNextLine()){
                searcher = scanner.nextLine();
                if(searcher.contains(aClass)){
                    searcher = searcher.replace("class:", "");
                    listOfCourseInfo.add(searcher.split(",")); //add course info as split lines
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
