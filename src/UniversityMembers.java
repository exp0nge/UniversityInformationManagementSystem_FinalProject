/**
 * Created by MD on 5/14/2015.
 */
public abstract class UniversityMembers {
    protected String emergencyContact;
    protected String address;
    protected String phoneNumber;

    protected  String firstName;
    protected  String lastName;
    protected  int IDNumber;

    protected void setEmergencyContact(String eContactString){
        this.emergencyContact = eContactString;
    }

    public int getID() {
        return this.IDNumber;
    }
}
