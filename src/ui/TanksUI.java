package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import tanks.AbstractTank;
import tanks.BT7;
import tanks.DestroyStaff;
import tanks.RandomTank;
import tanks.T34;
import tanks.Tiger;
import battle.ActionField;
import battle.BattleField;


public class TanksUI implements Observer {

    private JFrame f;
    private JPanel choicePanel;
    private DefenderChoicePanel defenderPanel;
    private AggressorChoicePanel aggressorPanel;
    private JPanel resultPanel;
    private ActionField af;
    private AbstractTank aggressor;
    private AbstractTank defender;
    private String aggressorScript = "Random";

    public TanksUI() throws Exception {
        this.af = null;

        f = new JFrame("TANKS, CHOICE AGGRESSOR");
        f.setLocation(300, 100);
        f.setMinimumSize(new Dimension(BattleField.BF_WIDTH,
                BattleField.BF_HEIGHT + 60));

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createChoicePanel();
        f.getContentPane().add(choicePanel);

        final JMenuItem mi = new JMenuItem("New Game");
        final JMenuItem ms = new JMenuItem("Stop Game");
        final JMenuItem ma = new JMenuItem("Select aggressor");
        final JMenuItem msh = new JMenuItem("Show Last Game");

        ms.setEnabled(false);
        ma.setEnabled(false);
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                runNewGame();
                mi.setEnabled(false);
                ms.setEnabled(true);
                ma.setEnabled(false);
            }

        });


        ms.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                af.stopFlag = true;
                mi.setEnabled(true);
                ms.setEnabled(false);
                ma.setEnabled(true);
            }

        });

        ma.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showChoicePanel();
                mi.setEnabled(true);
                ms.setEnabled(false);
                ma.setEnabled(false);
            }

        });

        msh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                runLastGame();
            }

        });

        JMenu m = new JMenu("Action");
        JMenu m1 = new JMenu("Depository");
        m.add(mi);
        m.add(ms);
        m.add(ma);
        m1.add(msh);
        JMenuBar mb = new JMenuBar();
        mb.add(m);
        mb.add(m1);
        f.getRootPane().setJMenuBar(mb);


        f.pack();
        f.setVisible(true);

    }
    private void createChoicePanel() {
        choicePanel = new JPanel();
        choicePanel.setLayout(new GridBagLayout());
        aggressorPanel = new AggressorChoicePanel();
        defenderPanel = new DefenderChoicePanel();
        choicePanel
                .add(defenderPanel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                        GridBagConstraints.LINE_START, 0,
                        new Insets(0, 0, 0, 0), 0, 0));
        choicePanel
                .add(aggressorPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                        GridBagConstraints.LINE_START, 0,
                        new Insets(0, 0, 0, 0), 0, 0));

    }

    private void createResultPanel(String res) {
        resultPanel = new JPanel();

        JLabel lName = new JLabel("RESULT:");
        resultPanel.add(lName, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel lRes = new JLabel(res);
        resultPanel.add(lRes, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

    }

    private void runNewGame() {
        try {
            setAggressorScript();
            f.getContentPane().removeAll();
            f.setTitle("TANKS, LEVEL 1");
            if (af == null) {
                af = new ActionField(aggressor,defender);
                af.result.addObserver(this);
            } else {
                af.restartActionField(aggressor,defender);
            }
            af.writeStartData();
            f.getContentPane().add(af);
            f.pack();
            f.repaint();
            Thread t = new Thread(af);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void runLastGame() {
        try {
            f.getContentPane().removeAll();
            f.setTitle("TANKS, LAST BATTLE");
            if (af == null) {
                af = new ActionField(null,null);
            }
            af.repeatActionField();
            af.result.addObserver(this);
            af.writeStartData();
            f.getContentPane().add(af);
            f.pack();
            f.repaint();
            Thread t = new Thread(af);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAggressorScript() {

        setTanks();

        aggressorScript = aggressorPanel.getAggressorScript();
        if (aggressorScript.equals("Random")) {
            aggressor.setRole(new RandomTank(aggressor));
        } else {
            aggressor.setRole(new DestroyStaff(aggressor));
        }
    }

    private void setTanks() {
        String aggressorName = aggressorPanel.getAggressorSelect();

        if (aggressorName.equals("t34top")) {
            aggressor = new T34();
        } else if (aggressorName.equals("tiger-top")) {
            aggressor = new Tiger();
        } else if (aggressorName.equals("bt7-top")) {
            aggressor = new BT7();
        } else {
            aggressor = new T34();
        }

        String defenderName = defenderPanel.getDefenderSelect();

        if (defenderName.equals("t34top")) {
            defender = new T34();
        } else if (defenderName.equals("tiger-top")) {
            defender = new Tiger();
        } else if (defenderName.equals("bt7-top")) {
            defender = new BT7();
        } else {
            defender = new T34();
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("UI was notified !");
        Result w = (Result) o;
        try {
            showResultPanel(w.getRes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        f.getRootPane().getJMenuBar().getMenu(0).getMenuComponents()[0].setEnabled(true);
        f.getRootPane().getJMenuBar().getMenu(0).getMenuComponents()[1].setEnabled(false);
        f.getRootPane().getJMenuBar().getMenu(0).getMenuComponents()[2].setEnabled(true);
    }

    public void showResultPanel(String str) throws Exception {
        Thread.sleep(1000);
        f.getContentPane().removeAll();
        f.setTitle("TANKS, RESULT");
        createResultPanel(str);
        f.getContentPane().add(resultPanel);
        f.pack();
        f.repaint();
    }

    public void showChoicePanel() {
        f.getContentPane().removeAll();
        f.setTitle("TANKS, CHOICE AGGRESSOR");
        f.getContentPane().add(choicePanel);
        f.pack();
        f.repaint();
    }
}
