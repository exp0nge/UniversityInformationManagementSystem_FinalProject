import javax.swing.*;

/**
 * Created by MD on 5/9/2015.
 */
public class StudentMode_UILayer {
    private static String username;
    private static StudentMode_UILayer inst; //Singleton
    private StudentMode_UILayer(){initComponents();}

    private void initComponents() {
        username = LoginForm_BLLayer.getUsername();
        JFrame frame = new JFrame("Student information for: " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setResizable(false);

        frame.setVisible(true);
    }

    public static StudentMode_UILayer getInst(){
        if(inst == null)
            inst = new StudentMode_UILayer();
        return inst;
    }


}
