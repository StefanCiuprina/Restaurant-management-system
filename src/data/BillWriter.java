package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BillWriter {

    public static void printBill(Map<Integer, Map<String, Double>> orders) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy:MM:dd: HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String dateFormat = date.replaceAll(":", "");
        dateFormat = dateFormat.replaceAll(" ", "");
        String bill = "bill" + dateFormat + ".txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(bill));
            double totalPrice = 0;
            for(int order : orders.keySet()) {
                writer.write("Order " + order + ":");
                writer.newLine();
                for(String product : orders.get(order).keySet()) {
                    double price = orders.get(order).get(product);
                    writer.write("\t" + product + " - $" + price);
                    writer.newLine();
                    totalPrice += price;
                }
            }
            writer.newLine();
            writer.write("TOTAL: $" + totalPrice);
            writer.newLine();
            writer.write("DATE: " + date);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
