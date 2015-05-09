import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by MD on 5/7/2015.
 */
public class LoginFormChooser_BLLayer {
    private static String userType;

    private LoginFormChooser_BLLayer(){}

    public static void setUserType(String seluserType, JFrame frame){
        userType = seluserType;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        LoginForm_UILayer loginForm = LoginForm_UILayer.getInst();

    }
}
