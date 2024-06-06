import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class SystemInventory {
    private final SystemInfo systemInfo = new SystemInfo();
    private final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private final OperatingSystem os = systemInfo.getOperatingSystem();
    private static final String SAVE_DIR = "/tmp"; // Update this to your preferred directory

    public String getUniqueId() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = os.toString() +
                    hardware.getProcessor().getProcessorIdentifier().toString() +
                    hardware.getMemory().getTotal();
//                    hardware.getGraphicsCards().stream().map(GraphicsCard::getName).collect(Collectors.joining(", ")) +
//                    System.getProperty("user.name") +
//                    hardware.getNetworkIFs().stream().map(NetworkIF::getMacaddr).collect(Collectors.joining(", "));
//            String data = "wiktor";
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String getOsNameAndVersion() {
        return os.toString();
    }

    public Map<String, String> getProcessorDetails() {
        CentralProcessor processor = hardware.getProcessor();
        CentralProcessor.ProcessorIdentifier identifier = processor.getProcessorIdentifier();
        Map<String, String> details = new HashMap<>();
        details.put("Name", identifier.getName());
        details.put("Identifier", identifier.getIdentifier());
        details.put("Model", identifier.getModel());
        details.put("Family", identifier.getFamily());
        details.put("Vendor", identifier.getVendor());
        details.put("Physical cores", Integer.toString(processor.getPhysicalProcessorCount()));
        details.put("Logical cores", Integer.toString(processor.getLogicalProcessorCount()));
        return details;
    }

    public Map<String, String> getMemory() {
        CentralProcessor processor = hardware.getProcessor();
        Map<String, String> details = new HashMap<>();
        details.put("Memory", String.valueOf(hardware.getMemory().getTotal()));
        return details;
    }

    public List<String> getGraphicsDetails() {
        return hardware.getGraphicsCards().stream()
                .map(gc -> "Name: " + gc.getName() + ", VRAM: " + gc.getVRam() / (1024 * 1024 * 1024) + " GB")
                .collect(Collectors.toList());
    }

    public String getPublicAndLocalIP() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return "Public IP: " + getPublicIP() + ", Local IP: " + localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return "Error retrieving IP address";
        }
    }

    public String getCurrentUser() {
        return System.getProperty("user.name");
    }

    // Method to retrieve the public IP address
    public String getPublicIP() {
        String publicIP = "Unable to retrieve IP";
        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            publicIP = in.readLine();  // read the first line which contains the public IP
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicIP;
    }

    // Method to get a list of all system users on Unix-based systems
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String line;
        try {
            Process process = Runtime.getRuntime().exec("cut -d: -f1 /etc/passwd");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                users.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

//    public static void main(String[] args) {
//        try {
//            takeScreenshot();
//            System.out.println("Screenshot taken successfully.");
//        } catch (IOException | AWTException e) {
//            System.out.println("Error taking screenshot: " + e.getMessage());
//        }
//    }



    private static String generateFileName(String basedir, String id, String prefix, String extension) {
        // Format the current time as a String
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = formatter.format(new Date());
        return basedir + "/" + id + "/" + prefix + "_" + timestamp + "." + extension;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Unique ID", getUniqueId());
        dataMap.put("OS and Version", getOsNameAndVersion());
        dataMap.put("Processor Details", getProcessorDetails());
        dataMap.put("Memory", getMemory());
        dataMap.put("Graphics Details", getGraphicsDetails());
        dataMap.put("IP Details", getPublicAndLocalIP());
        dataMap.put("Current User", getCurrentUser());
        dataMap.put("Users", getAllUsers());

        return gson.toJson(dataMap);
    }

    public static void takeScreenshot() throws AWTException, IOException {
        // Create a Robot instance to capture the screen
        SystemInventory inventory = new SystemInventory();
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        // Capture the screen image
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

        // Generate the file path with timestamp
        String fileName = generateFileName("/tmp", inventory.getUniqueId(), "screenshot", "png");
        File file = new File(fileName);

        // Make sure the directory path exists
        file.getParentFile().mkdirs();

        // Write the captured image as a PNG file
        ImageIO.write(screenFullImage, "png", file);
    }

    public static void writeTextToFile(String filePath, String text, boolean append) {
        File file = new File(filePath);
        // Ensure that the parent directory exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();  // Create the directory including any necessary but nonexistent parent directories
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.write(text);
            writer.newLine(); // Adds a new line after the written text
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public static void storeSystemInfo() {
        SystemInventory systemInventory = new SystemInventory();
        String fileName = generateFileName("/tmp", systemInventory.getUniqueId(), "sysinfo", "json");

        writeTextToFile(fileName, systemInventory.toJson(), true);

    }
}
