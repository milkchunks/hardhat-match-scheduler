//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class User_Interface {
    private final JFrame frame = new JFrame();
    private final JScrollPane inputScrollPane = new JScrollPane();
    private final JPanel ctrlWrapper = new JPanel();
    private final JButton generateSchedule = new JButton("Generate Schedule");
    private final Scheduler scheduler = new Scheduler();
    //this is fine being static i think
    //except i think that this should be abstract and all components should be static...
    private static final JTextField csvNotif = new JTextField("The output CSVs are in 'match scheduler csvs' in your C drive");

    private final JTable input = new JTable(25, 2) {
        public boolean isCellEditable(int row, int column) {return column == 1 && row > 0;}
    };
    private final JTable settings = new JTable(2, 2) {
        public boolean isCellEditable(int row, int column) {return column == 1;}
    };
    private final JTextField status = new JTextField("Ready");
    private final Font statusFont = new Font("Century Gothic Pro", Font.ITALIC, 32);
    private final Font inputFont = new Font("Century Gothic Pro", Font.BOLD, 32);
    private final Font settingsFont = new Font("Century Gothic Pro", Font.BOLD, 20);
    private final Font notifFont = new Font("Century Gothic Pro", Font.BOLD, 12);
    private final double TABLE_WEIGHT = 0.85;
    private final double CTRL_WEIGHT = 0.1;
    private final double NOTIF_WEIGHT = 0.05;

    public User_Interface() {
        this.frame.setTitle("Summer Camp Match Maker");
        this.frame.setBounds(0, 0, 1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Action generate = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                User_Interface.this.statusUpdate("Exporting Teams");
                csvNotif.setText("The output CSVs are in 'match scheduler csvs' in your C drive");
                //calls scheduler.prepareTeamCSV(), which then calls scheduler.prepareScheduleCSV()
                try {
                    scheduler.changeTeamNames(pollTeamNames());
                } catch (NumberFormatException exception) {
                    setNotifText("Please fill out the table to the right of the generate button.");
                }
                status.setText("Failed, error message below.");
            }
        };

        this.input.setValueAt("Team Number", 0, 0);
        this.input.setValueAt("Team Name", 0, 1);

        //24 rows
        for(int i = 1; i < 25; ++i) {
            this.input.setValueAt(i, i, 0);
        }

        input.setRowHeight((int) (TABLE_WEIGHT * frame.getHeight()) / input.getRowCount());

        //Settings Dialogue
        this.settings.setValueAt("Amount of Teams", 0, 0);
        this.settings.setValueAt("Matches per Team", 1, 0);
        settings.setRowHeight((int) (CTRL_WEIGHT * frame.getHeight()) / settings.getRowCount());

        //Setting fonts
        this.status.setFont(this.statusFont);
        this.generateSchedule.setFont(this.statusFont);
        this.input.setFont(this.inputFont);
        this.settings.setFont(this.settingsFont);

        //Ctrl wrapper layout (grid, all components are equal size)
        ctrlWrapper.setLayout(new GridLayout(1, 3));
        this.generateSchedule.setAction(generate);
        generateSchedule.setText("Generate Schedule");
        this.ctrlWrapper.add(this.generateSchedule);
        this.ctrlWrapper.add(this.status);
        this.ctrlWrapper.add(this.settings);
        ctrlWrapper.revalidate();
        ctrlWrapper.repaint();

        //Main pane layout, input table takes 70% of vertical space and control wrapper takes 30%
        frame.setLayout(new GridBagLayout());

        //auto-resize rows whenever the frame is resized
        frame.addComponentListener(new ComponentListener() {
                                       @Override
                                       public void componentResized(ComponentEvent e) {
                                            input.setRowHeight((int) (TABLE_WEIGHT * frame.getHeight()) / input.getRowCount() - 1);
                                            settings.setRowHeight((int) (CTRL_WEIGHT * frame.getHeight()) / settings.getRowCount());
                                       }

                                       //lazy
                                       @Override
                                       public void componentMoved(ComponentEvent e) {}

                                       @Override
                                       public void componentShown(ComponentEvent e) {}

                                       @Override
                                       public void componentHidden(ComponentEvent e) {}
                                   });
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = TABLE_WEIGHT;
        this.frame.getContentPane().add(input, c);
        c.gridy = 1;
        c.weighty = CTRL_WEIGHT;
        this.frame.getContentPane().add(this.ctrlWrapper, c);

        csvNotif.setEditable(false);
        csvNotif.setFont(notifFont);
        c.gridy = 2;
        c.weighty = NOTIF_WEIGHT;
        this.frame.getContentPane().add(csvNotif, c);
        //this.frame.setEnabled(true);
        this.status.setEditable(false);
        this.status.setEnabled(false);
        //this.frame.setVisible(true);
        //this.frame.setResizable(true);
        this.generateSchedule.setEnabled(true);
        this.status.setEnabled(true);
        //this.frame.repaint();
        frame.revalidate();
        frame.repaint();
        frame.pack();
        frame.setVisible(true);
    }

    public void statusUpdate(String message) {
        this.status.setText(message);
        this.status.updateUI();
    }

    public String[] pollTeamNames() {
        String[] output = new String[24];

        for(int i = 1; i < 25; ++i) {
            if (this.input.getValueAt(i, 1) == null) {
                output[i - 1] = "Practice Match";
            } else {
                int var10001 = i - 1;
                Object var10002 = this.input.getValueAt(i, 1);
                output[var10001] = "" + String.valueOf(var10002);
            }
        }

        return output;
    }

    //[amount of teams, matches per team]
    public String[] pollSettings() {
        String[] output = new String[2];
        output[0] = "" + String.valueOf(this.settings.getValueAt(0, 1));
        output[1] = "" + String.valueOf(this.settings.getValueAt(1, 1));
        return output;
    }

    public static void setNotifText(String text) {
        csvNotif.setText(text);
    }
}
