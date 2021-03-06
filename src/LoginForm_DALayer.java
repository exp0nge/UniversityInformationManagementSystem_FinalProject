import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by MD on 5/9/2015.
 */
public class LoginForm_DALayer {

    private LoginForm_DALayer(){}
    private static String [] infoLine;

    public static boolean checkPassword(String username, String password, String userType) {
        try {
            FileInputStream file;
            if(userType.equals("student")){
                file = new FileInputStream("db.csv");
            }else if(userType.equals("faculty")){
                file = new FileInputStream("dbAdmin.csv");
            }else
                file = null;
            String line;
            String [] accountInfoArray;
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains(username)){
                    accountInfoArray = line.split(",");
                    if (accountInfoArray[2].equals(username) && accountInfoArray[3].equals(password)) {
                        infoLine = line.split(",");
                        return true;
                    }
                }
            }

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }

    return false;
    }

    public static boolean registerNewEntity(JFrame frame, String firstName, String lastName, String username, String password, String type){
        boolean alreadyExists = checkPassword(username, password, type);
        if(alreadyExists == false) {
            String commaDelimiter = ",";
            String fullLine = "\n" + firstName + commaDelimiter + lastName + commaDelimiter + username + commaDelimiter + password + commaDelimiter + type + commaDelimiter + generateID();
            try {
                FileOutputStream file;
                if (type.equals("student"))
                    file = new FileOutputStream("db.csv", true);
                else
                    file = new FileOutputStream("dbAdmin.csv", true);
                file.write(fullLine.getBytes());
                FileOutputStream newFileUsername;
                if (type.equals("faculty"))
                    newFileUsername = new FileOutputStream("accounts/faculty/" + username + ".csv");
                else
                    newFileUsername = new FileOutputStream("accounts/st/" + username + ".csv");
                String header = "File information for: " + username;
                newFileUsername.write(header.getBytes());
                newFileUsername.write(fullLine.getBytes());
                JOptionPane.showMessageDialog(frame, "Account created!");
                file.close();
                newFileUsername.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }else{
            JOptionPane.showMessageDialog(frame, "Username already exists in database!");
            return false;
        }

        return true;
    }

    private static String generateID() {
        Random random = new Random(System.nanoTime());
        return Integer.toString(random.nextInt(1000000000));
    }

    public static String getUsername(){
        return infoLine[2];
    }
}
