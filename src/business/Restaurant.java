package business;

import business.menu.MenuItem;
import data.BillWriter;
import presentation.Observer;

import java.util.*;

public class Restaurant extends Observable implements IRestaurantProcessing {

    private Map<Order, Collection<MenuItem>> orders;
    private Map<String, MenuItem> menu;
    private int numberOrders;

    public Restaurant() {
        super();
        this.orders = new HashMap<>();
        this.menu = new HashMap<>();
        this.numberOrders = 0;
    }

    /**
     * Class invariant for the checking of the "object != null" precondition, checked with assert instruction
     * @param o the object that must be != null
     */
    private static void checkNullPrecondition(Object o) {
        assert o != null;
    }

    /**
     * Class invariant for the checking of the "ID >= 1" precondition, checked with assert instruction
     * @param ID the ID that must be >= 1
     */
    private static void checkIDPrecondition(int ID) {
        assert ID >= 1;
    }

    public MenuItem getMenuItem(String menuItemName) {
        return menu.get(menuItemName);
    }

    public void resetNumberOrders() {
        this.numberOrders = 0;
    }

    public String[][] getMenuString() {
        String[][] menuString = new String[menu.size()][4];
        int i = 0;
        for(MenuItem menuItem : menu.values()) {
            menuString[i][0] = menuItem.getName();
            if(menuItem.getClass().getSimpleName().equals("BaseProduct")) {
                menuString[i][1] = "Base Product";
                menuString[i][2] = "-";
            } else {
                menuString[i][1] = "Composite Product";
                StringBuilder sb = new StringBuilder();
                for(MenuItem compositeProductItem : menuItem.getMenuItems()) {
                    sb.append(compositeProductItem.getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.toString().length() - 1);
                menuString[i][2] = sb.toString();
            }
            menuString[i][3] = "$" + menuItem.computePrice();
            i++;
        }
        return menuString;
    }

    public String[][] getOrdersString() {
        String[][] ordersString = new String[orders.size()][3];
        int i = 0;
        for(Order order : orders.keySet()) {
            ordersString[i][0] = String.valueOf(order.getId());
            ordersString[i][1] = String.valueOf(order.getTableID());
            StringBuilder sb = new StringBuilder();
            for(MenuItem menuItem : orders.get(order)) {
                sb.append(menuItem.getName());
                sb.append(",");
            }
            sb.deleteCharAt(sb.toString().length() - 1);
            ordersString[i][2] = sb.toString();
            i++;
        }
        return ordersString;
    }

    @Override
    public boolean createMenuItem(MenuItem menuItem) {
        checkNullPrecondition(menuItem);
        if(!menu.containsKey(menuItem.getName())) {
            menu.put(menuItem.getName(), menuItem);
            return true;
    }
        return false;
    }

    @Override
    public boolean deleteMenuItem(String itemName) {
        checkNullPrecondition(itemName);
        if(menu.containsKey(itemName)) {
            menu.remove(itemName);
            return true;
        }
        return false;
    }

    @Override
    public boolean editMenuItem(MenuItem menuItem) {
        checkNullPrecondition(menuItem);
        if(menu.containsKey(menuItem.getName())) {
            menu.put(menuItem.getName(), menuItem);
            return true;
        }
        return false;
    }

    @Override
    public boolean createOrder(Observer chef, int tableID, Collection<String> menuItemsNames) {
        checkNullPrecondition(chef);
        checkIDPrecondition(tableID);
        checkNullPrecondition(menuItemsNames);
        this.numberOrders++;
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for(String menuItemName : menuItemsNames) {
            if(menu.containsKey(menuItemName)) {
                menuItems.add(menu.get(menuItemName));
                if(menu.get(menuItemName).getClass().getSimpleName().equals("CompositeProduct")) {
                    notifyObservers(chef, menu.get(menuItemName));
                }
            } else {
                return false;
            }
        }
        Order order = new Order(this.numberOrders, tableID);
        orders.put(order, menuItems);
        return true;
    }

    @Override
    public double computePrice(int orderID) {
        checkIDPrecondition(orderID);
        double price;
        Order order = new Order(orderID);
        if(orders.containsKey(order)) {
            price = 0;
            for(MenuItem menuItem : orders.get(order)) {
                price += menuItem.computePrice();
            }
        } else {
            price = -1;
        }
        return price;
    }

    @Override
    public boolean generateBill(int tableID) {
        checkIDPrecondition(tableID);
        boolean tableIDExists = false;
        Map<Integer, Map<String, Double>> ordersNames = new HashMap<>();
        Set<Order> toDelete = new HashSet<>();
        for(Order order : orders.keySet()) {
            if(order.getTableID() == tableID) {
                tableIDExists = true;
                Map<String, Double> products = new HashMap<>();
                for(MenuItem menuItem : orders.get(order)) {
                    products.put(menuItem.getName(), menuItem.computePrice());
                }
                toDelete.add(order);
                ordersNames.put(order.getId(), products);
            }
        }
        for(Order order : toDelete) {
            orders.remove(order);
        }
        if(tableIDExists) {
            BillWriter.printBill(ordersNames);
        }
        return tableIDExists;
    }

    public void deleteAllOrders() {
        orders.clear();
    }
}
