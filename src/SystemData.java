import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


public class SystemData {


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
