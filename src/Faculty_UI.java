import javax.swing.*;

/**
 * Created by MD on 5/14/2015.
 */
public class Faculty_UI extends JFrame {
    private static Faculty_UI inst;
    private Faculty_UI(){
        initComponents();
    }
    public static Faculty_UI getInst(){
        if(inst == null)
            inst = new Faculty_UI();
        return inst;
    }
    private void initComponents() {
    }
}

