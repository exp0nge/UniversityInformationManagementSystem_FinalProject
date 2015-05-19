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
    private static List<String> accountInfoList;
    private static Student student;

    private StudentMode_DALayer(){

    }

    public static void setUsername(String userName) {
        username = userName;
        setEntityInformation();
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
}
