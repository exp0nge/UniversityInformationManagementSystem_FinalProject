import java.util.List;

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

    public static String[] getListOfDepartments() {
        return StudentMode_DALayer.getListOfDepartments();
    }

    public static String[] getListOfCourses(String department) {
        return StudentMode_DALayer.getCourseList(department);
    }

    public static boolean savePlannedCourse(String deptComboBoxSelectedItem, String coursesOfferedInDeptSelectedItem) {
        return StudentMode_DALayer.savePlannedCourse(deptComboBoxSelectedItem, coursesOfferedInDeptSelectedItem);
    }

    public static String[] getArraySavedCourses() {
        return StudentMode_DALayer.getArraySavedCourses();
    }

    public static void clearSavedCourses() {
        StudentMode_DALayer.clearSavedCourseHistory();
    }
}
