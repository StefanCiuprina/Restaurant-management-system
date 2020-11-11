package presentation;

import java.util.ArrayList;

public interface Observer {

    void update(ArrayList<String> compositeProducts, ArrayList<String> dates);
}
