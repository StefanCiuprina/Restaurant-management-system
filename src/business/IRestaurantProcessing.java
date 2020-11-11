package business;

import business.menu.MenuItem;
import presentation.Observer;

import java.util.Collection;

/**
 * @invariant checkNullPrecondition(Object o) for checking the (o != null) precondition
 * @invariant checkIDPrecondition(int ID) for checking the (ID != null) precondition
 */

public interface IRestaurantProcessing {

    //administrator

    /**
     * @precondition menuItem != null;
     * @postcondition adds menuItem to menu Map of the object only if there's isn't already another menuItem object in the Map with the same name
     * @invariant using checkNullPrecondition(Object o) static method to verify the precondition
     * @param menuItem menuItem object to be added to the Map
     * @return true if operation is successful, false otherwise
     */
    boolean createMenuItem(MenuItem menuItem);
    /**
     * @precondition name != null;
     * @postcondition deletes menuItem from the Map of the object, returning false if the menuItem with the specified name is not found
     * @invariant using checkNullPrecondition(Object o) static method to verify the precondition
     * @param name the name of the menuItem to be deleted
     * @return true if operation is successful, false otherwise
     */
    boolean deleteMenuItem(String name);
    /**
     * @precondition menuItem != null;
     * @postcondition replaces the menuItem having the same name as the one sent as parameter from the Map of the object with the new menuItem, returning false if there's no menuItem having the required name
     * @invariant using checkNullPrecondition(Object o) static method to verify the precondition
     * @param menuItem gives the name of the menuItem to be replaces; is the replacement for the found item
     * @return true if operation is successful, false otherwise
     */
    boolean editMenuItem(MenuItem menuItem);


    //waiter

    /**
     * @precondition chef != null; tableID greater or equal than 1; menuItemsNames != null;
     * @postcondition creates a new order with a unique ID and the menuItems with the names given in the menuItemsNames collection, returns false if no such elements exist in the menu Map; also notifies the Observer chef with the objects of type CompositeProduct found in the menu Map
     * @invariant using checkNullPrecondition(Object o), checkIDPrecondition(int ID) static methods to verify the preconditions
     * @param chef Observer to be notified when CompositeProduct objects are found
     * @param tableID the tableID field the menuItem objects must have in order to be considered
     * @param menuItemsNames the names of the menuItem objects to be searched for in the menu Map
     * @return true if operation is successful, false otherwise
     */
    boolean createOrder(Observer chef, int tableID, Collection<String> menuItemsNames);
    /**
     * @precondition orderID greater or equal than 1
     * @postcondition adds the prices of all menuItem objects of an order from the orders Map, where the Order key has the orderID equal to the one given as parameter
     * @invariant using checkIDPrecondition(int ID) static method to verify the precondition
     * @param orderID the orderID the Order key must have to be considered
     * @return the total of the prices or -1 if there is no order having the orderID specified
     */
    double computePrice(int orderID);

    /**
     * @precondition tableID greater or equal than 1
     * @postcondition outputs a bill file containing all the orders from the orders Map having the tableID equal to the one given as parameter; if no order satisfying this is found, no file will be created and false will be returned
     * @invariant using checkIDPrecondition(int ID) static method to verify the precondition
     * @param tableID the tableID the orders must have to be considered
     * @return true if the operation is successful, false otherwise
     */
    boolean generateBill(int tableID);

}
