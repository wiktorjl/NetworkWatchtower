import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
    private final ScheduledExecutorService scheduler;

    public TaskScheduler(int corePoolSize) {
        this.scheduler = Executors.newScheduledThreadPool(corePoolSize);
    }

    public void startScheduling() {
        // Schedule a task to run every second
        scheduler.scheduleAtFixedRate(this::taskEverySecond, 0, 1, TimeUnit.SECONDS);

        // Schedule a task to run every minute
        scheduler.scheduleAtFixedRate(this::taskEveryMinute, 0, 1, TimeUnit.MINUTES);

        // Schedule a task to run every hour
        scheduler.scheduleAtFixedRate(this::taskEveryHour, 0, 1, TimeUnit.HOURS);
    }

    private void taskEverySecond() {
        System.out.println("Executing task every second: " + System.currentTimeMillis());
        SystemInventory si = new SystemInventory();
        try {
            si.takeScreenshot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SystemInventory.storeSystemInfo();
    }

    private void taskEveryMinute() {
        System.out.println("Executing task every minute: " + System.currentTimeMillis());
//        SystemInventory si = new SystemInventory();
//        SystemInventory.storeSystemInfo();
    }

    private void taskEveryHour() {
        System.out.println("Executing task every hour: " + System.currentTimeMillis());
    }

    public void stopScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        TaskScheduler scheduler = new TaskScheduler(3);
        scheduler.startScheduling();

        // Add shutdown hook to stop the scheduler when the program is terminated
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::stopScheduler));
    }
}
