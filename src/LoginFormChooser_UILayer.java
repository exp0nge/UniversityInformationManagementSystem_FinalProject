import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MD on 5/7/2015.
 */
public class LoginFormChooser_UILayer extends JFrame{
    private static LoginFormChooser_UILayer inst;
    private LoginFormChooser_UILayer(){
        initComponents();
    }
    public static LoginFormChooser_UILayer getGUI_Inst(){
        if(inst == null)
            inst = new LoginFormChooser_UILayer();
        return inst;
    }
    private void initComponents(){
        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        //Student button
        JButton studentButton = new JButton("Student");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.ipadx = 50;
        c.ipady = 30;
        c.fill = GridBagConstraints.BOTH;
        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFormChooser_BLLayer.setUserType("student", frame);
            }
        });
        panel.add(studentButton, c);

        //Faculty button
        JButton facultyButton = new JButton("Faculty");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        c.ipadx = 50;
        c.ipady = 30;
        c.fill = GridBagConstraints.BOTH;
        facultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFormChooser_BLLayer.setUserType("faculty", frame);
            }
        });
        panel.add(facultyButton, c);

        frame.setVisible(true);
    }

}
