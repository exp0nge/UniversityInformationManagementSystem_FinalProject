import java.util.ArrayList;
import java.util.List;

/**
 * Created by MD on 5/9/2015.
 */
public class Student {
    private final String firstName;
    private final String lastName;
    private final float GPA;
    private final int IDNumber;

    private List<String> borrowedBooks;

    public Student(String firstName, String lastName, float GPA, int IDNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.GPA = GPA;
        this.IDNumber = IDNumber;
        this.borrowedBooks = new ArrayList<String>();
    }

    public void setBorrowedBooksList(List<String> list){
        this.borrowedBooks = list;
    }


}
