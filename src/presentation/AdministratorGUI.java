package presentation;

import business.Restaurant;
import business.menu.BaseProduct;
import business.menu.CompositeProduct;
import business.menu.MenuItem;
import data.RestaurantSerializator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdministratorGUI extends JFrame {

    private Restaurant restaurant;

    private JLabel actionLabel;
    private JComboBox<String> actionComboBox;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel priceLabel;
    private JTextField priceTextField;
    private JLabel baseProductsLabel;
    private JTextField otherProductsTextField;
    private JLabel otherProductsTipLabel;
    private JButton applyButton;
    private JButton viewItemsButton;

    public AdministratorGUI() {

        restaurant = RestaurantSerializator.deserialize();

        JPanel panel = new JPanel();
        actionLabel = new JLabel("Choose your action:");
        String[] s1 = {"Create menu item", "Delete menu item", "Edit menu item"};
        actionComboBox = new JComboBox<>(s1);
        nameLabel = new JLabel("Product name:");
        nameTextField = new JTextField(30);
        priceLabel = new JLabel("Price (will be ignored if creating/editing a composite product):");
        priceTextField = new JTextField(30);
        baseProductsLabel = new JLabel("Base/Composite products (for creating/editing composite products; separate each item with a comma, no spaces)");
        otherProductsTextField = new JTextField(30);
        otherProductsTipLabel = new JLabel("*Leave empty for creating a base product");
        applyButton = new JButton("Apply");
        viewItemsButton = new JButton("View all menu items");

        panel.setLayout(new GridLayout(0,1));
        panel.add(actionLabel);
        panel.add(actionComboBox);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(priceLabel);
        panel.add(priceTextField);
        panel.add(baseProductsLabel);
        panel.add(otherProductsTextField);
        panel.add(otherProductsTipLabel);
        panel.add(applyButton);
        panel.add(viewItemsButton);
        this.setContentPane(panel);
        this.setSize(600, 400);
        this.setTitle("Administrator");
        this.setLocationRelativeTo(null);

        applyButton.addActionListener(new ApplyListener());
        viewItemsButton.addActionListener(new viewItemsListener());
    }

    public void openWindow() {
        this.setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "Error: " + message);
    }

    private void showSuccess() {
        JOptionPane.showMessageDialog(this, "Operation completed successfully");
    }

    private String getOperation() {
        try {
            return Objects.requireNonNull(actionComboBox.getSelectedItem()).toString();
        } catch(NullPointerException e) {
            return null;
        }
    }

    private String getMenuItemName() {
        return nameTextField.getText();
    }

    private String[] getOtherProductsNames() {
        if(otherProductsTextField.getText().isEmpty())
            return null;
        return otherProductsTextField.getText().split(",");
    }

    private double getPrice() {
        try {
            return Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private class ApplyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MenuItem menuItem;
            switch(Objects.requireNonNull(getOperation())) {
                case "Create menu item":
                    menuItem = getMenuItem();
                    if(menuItem != null) {
                        if(restaurant.createMenuItem(menuItem)) {
                            showSuccess();
                        } else {
                            showError("A menu item with this name already exists.");
                        }
                    }
                    break;
                case "Delete menu item":
                    if(restaurant.deleteMenuItem(getMenuItemName())) {
                        showSuccess();
                    } else {
                        showError("The menu item you want to delete doesn't exist.");
                    }
                    break;
                case "Edit menu item":
                    menuItem = getMenuItem();
                    if(menuItem != null) {
                        if(restaurant.editMenuItem(menuItem)) {
                            showSuccess();
                        } else {
                            showError("The menu item you want to edit doesn't exist.");
                        }
                    }
            }
            RestaurantSerializator.serialize(restaurant);
        }

        private MenuItem getMenuItem() {
            MenuItem menuItem;
            String[] otherProductsNames = getOtherProductsNames();
            if(otherProductsNames == null) { //base product
                double price = getPrice();
                if(price == -1) {
                    showError("Invalid price.");
                    return null;
                }
                menuItem = new BaseProduct(getMenuItemName(), price);
            } else { //composite product
                List<MenuItem> menuItems = new ArrayList<>();
                for(String otherProductName : otherProductsNames) {
                    MenuItem existingMenuItem = restaurant.getMenuItem(otherProductName);
                    if(existingMenuItem == null) {
                        showError("Base/Composite product " + otherProductName + " doesn't exist.");
                        return null;
                    }
                    menuItems.add(existingMenuItem);
                }
                menuItem = new CompositeProduct(getMenuItemName(), menuItems);
            }
            return menuItem;
        }
    }

    private class viewItemsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new MenuItemsTable(restaurant);
        }
    }

    static class MenuItemsTable extends JFrame {

        MenuItemsTable(Restaurant restaurant) {
            String[] columnNames = {"Name", "Type", "Components", "Price"};
            String[][] data = restaurant.getMenuString();
            JTable table = new JTable(data, columnNames);
            JScrollPane sp = new JScrollPane(table);
            this.add(sp);
            this.setSize(500, 200);
            this.setLocationRelativeTo(null);
            this.setTitle("Menu items");
            this.setVisible(true);
        }
    }



}
