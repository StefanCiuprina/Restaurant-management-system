package business;

import business.menu.MenuItem;
import presentation.Observer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Observable implements Serializable {

    private ArrayList<String> menuItemsNames;
    private ArrayList<String> dates;

    public Observable() {
        this.menuItemsNames = new ArrayList<>();
        this.dates = new ArrayList<>();
    }

    public void notifyObservers(Observer chef, MenuItem menuItem) {
        menuItemsNames.add(menuItem.getName());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        for(String ignored : menuItemsNames) {
            dates.add(date);
        }
        chef.update(menuItemsNames, dates);
    }

    public ArrayList<String> getMenuItemsNames() {
        return new ArrayList<>(menuItemsNames);
    }

    public ArrayList<String> getDates() {
        return new ArrayList<>(dates);
    }

    public void clearAll() {
        menuItemsNames.clear();
        dates.clear();
    }

}
