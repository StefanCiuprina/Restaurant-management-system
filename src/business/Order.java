package business;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable {

    private int id;
    private int tableID;

    public Order(int id, int tableID) {
        this.id = id;
        this.tableID = tableID;
    }


    //used when we want to get the items of an order
    //but we aren't actually interested in the table id
    //the actual order and an order with the table id -1 will
    //appear equal due to the override of equals
    public Order(int id) {
        this(id, -1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public int getTableID() {
        return tableID;
    }
}
