import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by MD on 5/9/2015.
 */
public class LoginForm_DALayer {
    public static boolean checkPassword(String username, String text) {
        try {
            FileInputStream file = new FileInputStream("db.csv");
            String line;
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(line.contains(username)){
                    if (line.contains(text)) {
                        return true;
                    }
                }
            }

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

    return false;
    }
}
