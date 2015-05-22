import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentMode_BLLayer {
    private static String username;
    private static String filepath;

    private StudentMode_BLLayer(){
    }

    public static String getUsername() {
        return username;
    }

    /**
     * Get student information list
     */
    public static void setStudentInfo() {
        username = LoginForm_BLLayer.getUsername();
        StudentMode_DALayer.setUsername(username);

    }

    /**
     * Slice student information list
     * @return sliced String
     */
    public static String getStudentName() {
        setStudentInfo();
        return StudentMode_DALayer.getName();
    }
    public static int getStudentID(){
        return StudentMode_DALayer.getID();
    }

    public static String getScholarships() {
        return StudentMode_DALayer.getScholarships();
    }


    public static String[] getEContactInfo() {
        return StudentMode_DALayer.getEContactInfo();
    }

    public static List<String []> getListOfCourseInfo() {
        return StudentMode_DALayer.getListOfCourseInfo();
    }
}
