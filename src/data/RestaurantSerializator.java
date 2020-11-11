package data;

import business.Restaurant;

import java.io.*;

public class RestaurantSerializator {

    public static void serialize(Restaurant r) {
        try {
            FileOutputStream fileOut = new FileOutputStream("Restaurant.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(r);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Restaurant deserialize() {
        Restaurant r = null;
        try {
            FileInputStream fileIn = new FileInputStream("Restaurant.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            r = (Restaurant) in.readObject();
            in.close();
            fileIn.close();
            return r;
        } catch (IOException i) {
            System.out.println("Restaurant.ser not found. A new one has been created.");
            r = new Restaurant();
            serialize(r);
            return r;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return new Restaurant();
        }
    }

}
