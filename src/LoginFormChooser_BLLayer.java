import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by MD on 5/7/2015.
 */
public class LoginFormChooser_BLLayer {
    private static String userType;
    private static String username;
    private static JFrame gFrame;

    private LoginFormChooser_BLLayer(){}

    public static void setUserType(String seluserType, JFrame frame){
        userType = seluserType;
        gFrame = frame;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        LoginForm_UILayer loginForm = LoginForm_UILayer.getInst();

    }

    public static void setPassword(String text) {
        boolean checkPassword = LoginForm_DALayer.checkPassword(username, text);
        if(checkPassword)
            JOptionPane.showMessageDialog(gFrame, "Password/Username a match!");
        else
            JOptionPane.showMessageDialog(gFrame, "Password/Username error!");
    }

    public static void setUsername(String text) {
        username = text;
    }
}
