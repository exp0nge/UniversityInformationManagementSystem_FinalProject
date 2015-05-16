/**
 * Created by MD on 5/14/2015.
 */
public class Faculty extends UniversityMembers{

    private String department;

    public Faculty(String firstName, String lastName, int ID, String department){
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = ID;
        this.department = department;
    }
    
    public String getName(){
        return firstName + " " + lastName;
    }

    public void changeDepartment(String department){
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
