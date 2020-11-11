package business.menu;

import java.util.ArrayList;
import java.util.List;

public class CompositeProduct extends MenuItem {

    private List<MenuItem> menuItems;

    public CompositeProduct(String name, List<MenuItem> menuItems) {
        this.name = name;
        this.price = 0;
        this.menuItems = new ArrayList<>(menuItems);
    }

    @Override
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public double computePrice() {
        this.price = 0;
        for(MenuItem menuItem : menuItems) {
            this.price += menuItem.computePrice();
        }
        return this.price;
    }
}
