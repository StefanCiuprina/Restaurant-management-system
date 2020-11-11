package presentation;

import business.Restaurant;
import data.RestaurantSerializator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChefGUI extends JFrame implements Observer {

    private ArrayList<String> compositeProducts;
    private ArrayList<String> dates;

    private JPanel panel;

    private JButton clearAllButton;
    private JTable table;
    private JScrollPane sp;
    private final String[] columnNames = {"Composite Product", "Time added"};

    public ChefGUI() {

        panel = new JPanel();

        panel.setLayout(new GridLayout(0,1));

        clearAllButton = new JButton("Clear all");
        panel.add(clearAllButton);

        Restaurant restaurant = RestaurantSerializator.deserialize();
        compositeProducts = restaurant.getMenuItemsNames();
        dates = restaurant.getDates();
        String[][] data = new String[compositeProducts.size()][2];
        for(int i = 0; i < compositeProducts.size(); i++) {
            data[i][0] = compositeProducts.get(i);
            data[i][1] = dates.get((i));
        }
        table = new JTable(data, columnNames);
        sp = new JScrollPane(table);
        panel.add(sp);

        this.setContentPane(panel);
        this.setSize(600, 200);
        this.setTitle("Chef");
        this.setLocationRelativeTo(null);

        clearAllButton.addActionListener(new clearAllListener());
    }

    private class clearAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Restaurant restaurant = RestaurantSerializator.deserialize();
            restaurant.clearAll();
            RestaurantSerializator.serialize(restaurant);
            clear();
        }
    }

    public void openWindow() {
        this.setVisible(true);
    }

    @Override
    public void update(ArrayList<String> compositeProducts, ArrayList<String> dates) {
        this.compositeProducts = compositeProducts;
        this.dates = dates;
        display();
    }

    private void clear() {
        panel.remove(sp);
        String[][] data = new String[0][2];
        table = new JTable(data, columnNames);
        sp = new JScrollPane(table);
        panel.add(sp);
        this.setContentPane(panel);
    }

    private void display() {
        panel.remove(sp);
        String[][] data = new String[compositeProducts.size()][2];
        for(int i = 0; i < compositeProducts.size(); i++) {
            data[i][0] = compositeProducts.get(i);
            data[i][1] = dates.get((i));
        }
        table = new JTable(data, columnNames);
        sp = new JScrollPane(table);
        panel.add(sp);
        this.setContentPane(panel);
    }
}
