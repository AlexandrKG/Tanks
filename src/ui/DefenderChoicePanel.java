package ui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DefenderChoicePanel  extends JPanel {
    private static String t34 = "T34";
    private static String tiger = "Tiger";
    private static String bt7 = "BT7";

    private String defenderSelect;
    private JLabel picture;
    private class RBListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            defenderSelect = e.getActionCommand();
            picture.setIcon(createImageIcon(defenderSelect + ".PNG"));
        }

    }

    public DefenderChoicePanel() {

        this.setLayout(new GridBagLayout());

        JLabel lProducts = new JLabel("CHOICE TANKS:");
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
        rb1.setSelected(true);

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

        //Set up the picture label.
        defenderSelect = "t34top";
        picture = new JLabel(createImageIcon(defenderSelect + ".PNG"));
        picture.setPreferredSize(new Dimension(64, 64));



        add(lProducts, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));

        add(radioPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));


        add(picture, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, 0,
                new Insets(0, 0, 0, 0), 0, 0));


    }

    private ImageIcon createImageIcon(String path) {
        String filePath = "pictures/" + path;
        Image image = null;
        try {
            image = ImageIO.read(new File(filePath).getAbsoluteFile());
            if (image != null) {
                return new ImageIcon(image);
            } else {
                System.err.println("Couldn't find file: " + filePath);
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDefenderSelect() {
        return defenderSelect;
    }
}
