import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.io.IOException;

public class FileMonitor {
    private final WatchService watcher;
    private final Path dirIncoming;
    private final Path dirOutgoing;
    private final String incomingFileName;
    private final String outgoingFileName;

    public FileMonitor(String incomingFilePath, String outgoingFilePath) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        Path incomingPath = Paths.get(incomingFilePath);
        Path outgoingPath = Paths.get(outgoingFilePath);

        this.incomingFileName = incomingPath.getFileName().toString();
        this.outgoingFileName = outgoingPath.getFileName().toString();

        // Handle potential null parent paths
        this.dirIncoming = incomingPath.getParent() != null ? incomingPath.getParent() : Paths.get("").toAbsolutePath();
        this.dirOutgoing = outgoingPath.getParent() != null ? outgoingPath.getParent() : Paths.get("").toAbsolutePath();

        // Register directories with the watcher
        dirIncoming.register(watcher, ENTRY_MODIFY);
        dirOutgoing.register(watcher, ENTRY_MODIFY);
    }

    public void startMonitoring() {
        Thread monitorThread = new Thread(() -> {
            while (true) {
                // Wait for key to be signalled
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // Context for directory entry event is the file name of entry
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    if (kind == ENTRY_MODIFY) {
                        if (fileName.toString().equals(incomingFileName)) {
                            reloadFile(dirIncoming.resolve(fileName).toString());
                        } else if (fileName.toString().equals(outgoingFileName)) {
                            reloadFile(dirOutgoing.resolve(fileName).toString());
                        }
                    }
                }

                // Reset key and remove from set if directory no longer accessible
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        });

        monitorThread.start();
    }

    private void reloadFile(String filePath) {
        System.out.println("File changed: " + filePath);
        // Implement reloading logic here
    }

    public static void main(String[] args) {
        try {
            FileMonitor monitor = new FileMonitor("incoming.csv", "outgoing.csv");
            monitor.startMonitoring();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
