import java.awt.*;
import java.io.IOException;

public class Collector {

    public static void main(String[] args) {
        SystemInventory inventory = new SystemInventory();
        System.out.println(inventory.toJson());

        try {
            SystemInventory.takeScreenshot();
            System.out.println("Screenshot taken successfully.");
        } catch (IOException | AWTException e) {
            System.out.println("Error taking screenshot: " + e.getMessage());
        }
    }
}
