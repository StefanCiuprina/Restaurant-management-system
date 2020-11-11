package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {

    private JButton administratorButton;
    private JButton waiterButton;
    private JButton chefButton;

    private AdministratorGUI administratorGUI;
    private WaiterGUI waiterGUI;
    private ChefGUI chefGUI;

    public MainGUI() {
        administratorGUI = new AdministratorGUI();
        chefGUI = new ChefGUI();
        waiterGUI = new WaiterGUI(chefGUI);

        administratorButton = new JButton("Administrator");
        waiterButton = new JButton("Waiter");
        chefButton = new JButton("Chef");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(administratorButton);
        panel.add(waiterButton);
        panel.add(chefButton);

        this.setContentPane(panel);
        this.setTitle("Restaurant Management System");
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        administratorButton.addActionListener(new administratorListener());
        waiterButton.addActionListener(new waiterListener());
        chefButton.addActionListener(new chefListener());
    }

    private class administratorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            administratorGUI.openWindow();
        }
    }

    private class waiterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            waiterGUI.openWindow();
        }
    }

    private class chefListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            chefGUI.openWindow();
        }
    }

}
