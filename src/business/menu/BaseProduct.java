package business.menu;

import java.util.List;

public class BaseProduct extends MenuItem {

    public BaseProduct(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public BaseProduct(String name) {
        this(name, 0);
    }

    @Override
    public List<MenuItem> getMenuItems() {
        return null;
    }

    @Override
    public double computePrice() {
        return price;
    }
}
