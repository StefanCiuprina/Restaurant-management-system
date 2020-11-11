package business.menu;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class MenuItem implements Serializable {

    String name;
    double price;

    public String getName() {
        return this.name;
    }

    public abstract double computePrice();
    public abstract List<MenuItem> getMenuItems();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(name, menuItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
