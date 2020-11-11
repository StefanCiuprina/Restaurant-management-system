package presentation;

import business.Order;
import business.Restaurant;
import data.RestaurantSerializator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class WaiterGUI {

    private JFrame frame;

    private Restaurant restaurant;

    private JButton createOrderButton;
    private JButton computePriceButton;
    private JButton generateBillButton;
    private JButton resetOrdersButton;
    private JTable table;
    private final String[] columnNames = { "OrderID", "Table ID", "Products ordered"};
    private JScrollPane sp;
    private JPanel panel;

    private Observer chef;

    public WaiterGUI(Observer chef) {

        frame = new JFrame();

        restaurant = RestaurantSerializator.deserialize();
        this.chef = chef;

        panel = new JPanel();
        createOrderButton = new JButton("Create order");
        computePriceButton = new JButton("Compute price for an order");
        generateBillButton = new JButton("Generate bill");
        resetOrdersButton = new JButton("Reset orders");
        String[][] data = restaurant.getOrdersString();
        table = new JTable(data, columnNames);
        sp = new JScrollPane(table);

        panel.setLayout(new GridLayout(0,1));
        panel.add(createOrderButton);
        panel.add(computePriceButton);
        panel.add(generateBillButton);
        panel.add(resetOrdersButton);
        panel.add(sp);

        frame.setContentPane(panel);
        frame.setSize(600, 400);
        frame.setTitle("Waiter");
        frame.setLocationRelativeTo(null);

        createOrderButton.addActionListener(new createOrderListener());
        computePriceButton.addActionListener(new computePriceListener());
        generateBillButton.addActionListener(new generateBillListener());
        resetOrdersButton.addActionListener(new resetOrdersListener());
    }

    public void openWindow() {
        frame.setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, "Error: " + message);
    }

    private void showSuccess() {
        JOptionPane.showMessageDialog(frame, "Operation completed successfully");
    }

    private void showPrice(double price) {
        JOptionPane.showMessageDialog(frame, "The order total is $" + price);
    }

    private class createOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new createOrderFrame();
        }
    }

    private class createOrderFrame extends JFrame {

        private JLabel tableIDLabel;
        private JTextField tableIDTextField;
        private JLabel menuItemsLabel;
        private JTextField menuItemsTextField;
        private JButton viewMenuItemsButton;
        private JButton createButton;

        private createOrderFrame() {
            tableIDLabel = new JLabel("Table ID:");
            tableIDTextField = new JTextField();
            menuItemsLabel = new JLabel("Menu items, separated by comma (no spaces):");
            menuItemsTextField = new JTextField();
            viewMenuItemsButton = new JButton("View menu items");
            createButton = new JButton("Create Order");

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1));
            panel.add(tableIDLabel);
            panel.add(tableIDTextField);
            panel.add(menuItemsLabel);
            panel.add(menuItemsTextField);
            panel.add(viewMenuItemsButton);
            panel.add(createButton);

            this.setContentPane(panel);
            this.setSize(300,300);
            this.setTitle("Create order");
            this.setLocationRelativeTo(null);
            this.setVisible(true);

            viewMenuItemsButton.addActionListener(new viewItemsListener());
            createButton.addActionListener(new createListener());
        }

        private int getTableID() {
            try {
                int n = Integer.parseInt(tableIDTextField.getText());
                if(n <= 0) {
                    throw new NumberFormatException();
                }
                return n;
            } catch(NumberFormatException e) {
                return -1;
            }
        }

        private ArrayList<String> getMenuItemsNames() {
            String[] items = menuItemsTextField.getText().split(",");
            return new ArrayList<>(Arrays.asList(items));
        }

        private class viewItemsListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                restaurant = RestaurantSerializator.deserialize();
                new AdministratorGUI.MenuItemsTable(restaurant);
            }
        }

        private class createListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                restaurant = RestaurantSerializator.deserialize();
                if(restaurant.createOrder(chef, getTableID(), getMenuItemsNames())) {
                    showSuccess();
                    RestaurantSerializator.serialize(restaurant);
                    updateTable();
                } else {
                    showError("One of the menu items entered doesn't exist.\nPlease check the menu items and that there are no spaces between products (only commas are needed)");
                }
            }
        }

    }

    private void updateTable() {
        panel.remove(sp);
        String[][] data = restaurant.getOrdersString();
        table = new JTable(data, columnNames);
        sp = new JScrollPane(table);
        panel.add(sp);
        frame.setContentPane(panel);
    }

    private class computePriceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new computePriceFrame();
        }
    }

    private class computePriceFrame extends JFrame {

        private JLabel orderID;
        private JTextField orderIDTextField;
        private JButton computeButton;

        private computePriceFrame() {
            orderID = new JLabel("Order ID:");
            orderIDTextField = new JTextField();
            computeButton = new JButton("Compute price");

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1));
            panel.add(orderID);
            panel.add(orderIDTextField);
            panel.add(computeButton);

            this.setContentPane(panel);
            this.setSize(300,150);
            this.setTitle("Compute price for an order");
            this.setLocationRelativeTo(null);
            this.setVisible(true);

            computeButton.addActionListener(new computeListener());
        }

        private int getOrderID() {
            try {
                return Integer.parseInt(orderIDTextField.getText());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private class computeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int orderID = getOrderID();
                double price;
                if((orderID != -1) && ((price = restaurant.computePrice(orderID)) != -1)) {
                    showPrice(price);
                } else {
                    showError("Invalid order ID.");
                }
            }
        }

    }

    private class generateBillListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new generateBillFrame();
        }
    }

    private class generateBillFrame extends JFrame {
        private JLabel tableID;
        private JTextField tableIDTextField;
        private JButton generateButton;

        private generateBillFrame() {

            JPanel panel = new JPanel();
            tableID = new JLabel("Table ID:");
            tableIDTextField = new JTextField();
            generateButton = new JButton("Generate bill");


            panel.setLayout(new GridLayout(0, 1));
            panel.add(tableID);
            panel.add(tableIDTextField);
            panel.add(generateButton);

            this.setContentPane(panel);
            this.setSize(300,150);
            this.setTitle("Generate bill");
            this.setLocationRelativeTo(null);
            this.setVisible(true);

            generateButton.addActionListener(new generateListener());
        }

        private int getTableID() {
            try {
                return Integer.parseInt(tableIDTextField.getText());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private class generateListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableID = getTableID();
                if((tableID != -1) && restaurant.generateBill(tableID)) {
                    showSuccess();
                    updateTable();
                } else {
                    showError("Invalid table ID.");
                }
            }
        }
    }

    private class resetOrdersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to reset orders?","Warning", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                restaurant.deleteAllOrders();
                restaurant.resetNumberOrders();
                RestaurantSerializator.serialize(restaurant);
                updateTable();
            }
        }
    }

}
