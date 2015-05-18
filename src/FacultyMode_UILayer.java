import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by MD on 5/15/2015.
 */
public class FacultyMode_UILayer extends JFrame {
    private static String username;
    private static FacultyMode_UILayer inst;
    private FacultyMode_UILayer(){
        username = LoginForm_BLLayer.getUsername();
        FacultyMode_BLLayer.setUsername(username);
        initComponents();
    }

    private void initComponents() {
        Dimension minSizeUI = new Dimension(500, 450);
        JFrame frame = new JFrame("Faculty mode");
        frame.setSize(540, 560);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);


        JPanel pane = new JPanel(new MigLayout());
        frame.getContentPane().add(pane);

        //Hello "user" label
        String helloString = "Hello " + FacultyMode_BLLayer.getName() + ",";
        JLabel helloNameLabel = new JLabel(helloString);

        //Jtabbed
        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent searchComponent = new JPanel(new MigLayout());

        //Search tool
        JTextField searchTF = new JTextField("Search for student...");
        searchTF.setMinimumSize(searchTF.getPreferredSize());
        searchTF.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchTF.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        MigLayout searchPanelManager = new MigLayout(
                new LC().wrapAfter(6)
        );
        JPanel searchResultsPanel = new JPanel(searchPanelManager);
        JButton searchButton = new JButton("GO");
        searchButton.addActionListener(ae -> {
            searchButton.setEnabled(false); //turn off GO button
            FacultyMode_BLLayer.searchForStudent(searchTF.getText(), searchResultsPanel);
            searchButton.setEnabled(true);
        });
        searchComponent.setMinimumSize(minSizeUI);
        searchComponent.add(searchTF, "span 4");
        searchComponent.add(searchButton, "wrap");
        searchComponent.add(searchResultsPanel, "span");
        tabbedPane.addTab("Students", searchComponent);

        pane.add(helloNameLabel, "wrap");
        pane.add(tabbedPane, "span");

        frame.setVisible(true);

    }

    public static FacultyMode_UILayer getInst() {
        if(inst == null)
            inst = new FacultyMode_UILayer();
        return inst;
    }


}
