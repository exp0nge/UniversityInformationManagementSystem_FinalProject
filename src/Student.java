import java.util.ArrayList;
import java.util.List;

/**
 * Created by MD on 5/9/2015.
 */
public class Student extends UniversityMembers{

    private final double GPA;
    private float tuitionDue;


    private String major;
    private String minor;


    private int studentRank;


    private List<String> borrowedBooks;

    protected Student(String firstName, String lastName, double GPA, int IDNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.GPA = GPA;
        this.IDNumber = IDNumber;
        this.borrowedBooks = new ArrayList<String>();
    }



    protected void setMajor(String major){
        this.major = major;
    }
    protected void setMinor(String minor){
        this.minor = minor;
    }

    protected String getName(){
        return this.firstName + " " + this.lastName;
    }

    public void setBorrowedBooksList(List<String> list){
        this.borrowedBooks = list;
    }


    public float getTuitionDue() {
        return tuitionDue;
    }

    public void setTuitionDue(float tuitionDue) {
        this.tuitionDue = tuitionDue;
    }

    public double getGPA() {
        return GPA;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public int getStudentRank() {
        return studentRank;
    }

    public void setStudentRank(int studentRank) {
        this.studentRank = studentRank;
    }


}
