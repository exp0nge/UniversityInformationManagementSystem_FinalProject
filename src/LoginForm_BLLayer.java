import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by MD on 5/7/2015.
 */
public class LoginForm_BLLayer {
    private static String userType;
    private static String username;
    private static JFrame gFrame;

    private LoginForm_BLLayer(){}

    public static void setUserType(String seluserType, JFrame frame){
        userType = seluserType.toLowerCase();
        gFrame = frame;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        LoginForm_UILayer loginForm = LoginForm_UILayer.getInst();

    }

    public static void setPassword(String password, JFrame authFormFrame) {

        boolean checkPassword = LoginForm_DALayer.checkPassword(username, password, userType);

        if(checkPassword) {
            authFormFrame.dispatchEvent(new WindowEvent(authFormFrame, WindowEvent.WINDOW_CLOSING));
            if(userType.equals("faculty")){
                //TODO
            }
            else{
                StudentMode_UILayer studentUI = StudentMode_UILayer.getInst();
            }
        }
        else
            JOptionPane.showMessageDialog(gFrame, "Password/Username error!");
    }

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername(){
        return username;
    }

    public static void newRegister() {
        Register_UILayer registerUI = Register_UILayer.getInst(userType);
    }

    public static void registerNewEntity(JFrame frame, String firstNameTF, String lastNameTF, String usernameTF, String passwordTF) {
        LoginForm_DALayer.registerNewEntity(frame, firstNameTF, lastNameTF, usernameTF, passwordTF, userType);
    }
}
