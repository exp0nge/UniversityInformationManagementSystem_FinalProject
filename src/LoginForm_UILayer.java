import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MD on 5/7/2015.
 */
public class LoginForm_UILayer extends JFrame {
    private static LoginForm_UILayer inst;

    private LoginForm_UILayer(){
        initComponents();
    }
    public static LoginForm_UILayer getInst(){
        if(inst == null)
            inst = new LoginForm_UILayer();
        return inst;
    }

    private void initComponents(){
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 350);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        //City College icon
        JLabel ccnyIcon = new JLabel();
        ImageIcon img = new ImageIcon("ccnyLogo.png");
        ccnyIcon.setIcon(img);
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 40;
        panel.add(ccnyIcon, c);

        c.ipady = 0; //reset

        //Username label
        JLabel usernameLabel = new JLabel("Username:");
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 10;
        panel.add(usernameLabel, c);

        //Username entry
        JTextField usernameTextF = new JTextField();
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 5;
        c.gridheight = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameTextF, c);

        //Password label
        JLabel passwordLabel = new JLabel("Password:");
        c.gridx = 0;
        c.gridy = 5;
        c.ipadx = 10;
        panel.add(passwordLabel, c);

        //Password entry
        JTextField passwordTextF = new JTextField();
        c.gridx = 1;
        c.gridy = 5;

        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordTextF, c);

        //Submit button
        JButton submitButton = new JButton("Submit");
        c.gridx = 1;
        c.gridy = 10;
        c.gridheight = 3;
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFormChooser_BLLayer.setUsername(usernameTextF.getText());
                LoginFormChooser_BLLayer.setPassword(passwordTextF.getText());
            }
        });
        panel.add(submitButton, c);

        //Register button
        JButton registerButton = new JButton("Register");
        c.gridx = 1;
        c.gridy = 14;
        c.insets = new Insets(0, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        panel.add(registerButton, c);

        frame.setVisible(true);
    }

}
