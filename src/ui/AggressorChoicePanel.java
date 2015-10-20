package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

public class AggressorChoicePanel extends JPanel {

    private static String t34 = "T34";
    private static String tiger = "Tiger";
    private static String bt7 = "BT7";

    private String aggressorSelect;
    private JLabel picture;
    private String aggressorScript = "Random";

    private class RBListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            aggressorSelect = e.getActionCommand();
            picture.setIcon(createImageIcon(aggressorSelect + ".PNG"));
        }

    }

    private class BAListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            aggressorScript = e.getActionCommand();

        }

    }

    public AggressorChoicePanel() {
        setLayout(new GridBagLayout());

        JLabel lProducts = new JLabel("CHOICE AGGRESSOR:");
        final ButtonGroup group = new ButtonGroup();

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(3, 0));
        radioPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        ActionListener rbListner = new RBListener();

        JRadioButton rb1 = new JRadioButton(t34);
        group.add(rb1);
        radioPanel.add(rb1);
        rb1.setActionCommand("t34top");
        rb1.addActionListener(rbListner);


        JRadioButton rb2 = new JRadioButton(tiger);
        group.add(rb2);
        radioPanel.add(rb2);
        rb2.setActionCommand("tiger-top");
        rb2.addActionListener(rbListner);

        JRadioButton rb3 = new JRadioButton(bt7);
        group.add(rb3);
        radioPanel.add(rb3);
        rb3.setActionCommand("bt7-top");
        rb3.addActionListener(rbListner);
        rb3.setSelected(true);

        //Set up the picture label.
        aggressorSelect = "bt7-top";
        picture = new JLabel(createImageIcon(aggressorSelect + ".PNG"));
        picture.setPreferredSize(new Dimension(64, 64));

        JPanel behaviourAgrPanel = new JPanel();
        behaviourAgrPanel.setLayout(new GridLayout(2, 0));
        behaviourAgrPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        ActionListener baListner = new BAListener();
        final ButtonGroup groupBA = new ButtonGroup();

        JRadioButton ba1 = new JRadioButton("Random Script");
        groupBA.add(ba1);
        behaviourAgrPanel.add(ba1);
        ba1.setActionCommand("Random");
        ba1.addActionListener(baListner);
        ba1.setSelected(true);

        JRadioButton ba2 = new JRadioButton("Destroy Staff");
        groupBA.add(ba2);
        behaviourAgrPanel.add(ba2);
        ba2.setActionCommand("DestroyStaff");
        ba2.addActionListener(baListner);


        add(lProducts, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));

        add(radioPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));

        add(behaviourAgrPanel, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));

        add(picture, new GridBagConstraints(2, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));

    }

    private ImageIcon createImageIcon(String path) {
        String filePath = "/resources/pictures/" + path;
        URL url = null;
        try {
            url = getClass().getResource(filePath);
            if (url != null) {
                return new ImageIcon(url);
            } else {
                System.err.println("Couldn't find file: " + filePath);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAggressorSelect() {
        return aggressorSelect;
    }

    public String getAggressorScript() {
        return aggressorScript;
    }
}
