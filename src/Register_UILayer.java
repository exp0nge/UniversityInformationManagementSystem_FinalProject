import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MD on 5/9/2015.
 */
public class Register_UILayer {
    private static String userType;
    private static Register_UILayer inst;
    private Register_UILayer(){ initComponents();}
    public static Register_UILayer getInst(String usertype){
        if(inst == null)
            inst = new Register_UILayer();
        userType = usertype;
        return inst;
    }

    private void initComponents() {
        JFrame frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        //Username components
        JLabel usernameL = new JLabel("Username: ");
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameL, c);
        JTextField usernameTF = new JTextField();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameTF, c);

        //first Name
        JLabel firstNameL = new JLabel("First name: ");
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 5;
        panel.add(firstNameL, c);
        JTextField firstNameTF = new JTextField();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(firstNameTF, c);

        //last Name
        JLabel lastNameL = new JLabel("Last name: ");
        c.gridx = 0;
        c.gridy = 2;
        panel.add(lastNameL, c);
        JTextField lastNameTF = new JTextField();
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lastNameTF, c);

        //password
        JLabel passwordL = new JLabel("Password: ");
        c.gridx = 0;
        c.gridy = 3;
        panel.add(passwordL, c);
        JPasswordField passwordTF = new JPasswordField();
        passwordTF.setEchoChar('*');
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordTF, c);

        //Submit button
        JButton submitButton = new JButton("Submit");
        c.gridx = 1;
        c.gridy = 5;
        c.fill = GridBagConstraints.NONE;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstNameTF.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "First name cannot be blank.");
                }
                else if(lastNameTF.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Last name cannot be blank.");
                }
                else  if(usernameTF.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Username cannot be blank.");
                }
                else if(passwordTF.getPassword().length < 3){
                    JOptionPane.showMessageDialog(null, "Password cannot be blank/less than 3 characters.");
                }
               else {
                    String password = "";
                    for(char i : passwordTF.getPassword()){
                        password += i;
                    }
                    LoginForm_BLLayer.registerNewEntity(frame, firstNameTF.getText(), lastNameTF.getText(), usernameTF.getText(), password);
                }
            }
        });
        panel.getRootPane().setDefaultButton(submitButton);
        panel.add(submitButton, c);

        frame.setVisible(true);
    }


}
