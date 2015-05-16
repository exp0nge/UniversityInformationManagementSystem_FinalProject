import java.util.List;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentFaculty_BLLayer {
    private static String username;

    private StudentFaculty_BLLayer(){
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
        return "Hello, " + StudentMode_DALayer.getName();
    }
    public static int getStudentID(){
        return StudentMode_DALayer.getID();
    }
}
