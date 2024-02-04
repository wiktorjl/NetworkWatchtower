import java.net.Inet4Address;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


public abstract class SystemData {

    public abstract List<String> getInterfaces();

    public Inet4Address getAddressForInterface(String iface) {
        return null;
    }

    public long getInBytesForInterface(String iface) {
        return 23430;
    }

    public long getOutBytesForInterface(String iface) {
        return 123;
    }

    public IConnectionListModel<String> getOutConnections() {
        return null;
    }

    public IConnectionListModel<String> getInConnections() {
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Timer timer = new Timer();

        // Define the task to be executed every second
        TimerTask task = new TimerTask() {
            private int count = 50; // Run the task 5 times for demonstration

            @Override
            public void run() {
                if (count > 0) {
                    System.out.println("Background task executed at: " + System.currentTimeMillis());
                    count--;
                } else {
                    timer.cancel(); // Stop the timer when the task is done
                    latch.countDown(); // Release the latch to allow the main thread to exit
                }
            }
        };

        // Schedule the task to run every second (1000 milliseconds)
        timer.scheduleAtFixedRate(task, 0, 1000);

        // Wait for the timer task to stop
        latch.await();
        System.out.println("Background task stopped.");
    }

}
