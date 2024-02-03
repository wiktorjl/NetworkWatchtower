import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkingInfoPanel extends JPanel {

    private JTextArea textArea;

    public NetworkingInfoPanel() {
        setLayout(new BorderLayout());
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);

        displayNetworkInfo();
    }

    private void displayNetworkInfo() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                textArea.append("Interface Name: " + networkInterface.getDisplayName() + "\n");

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    textArea.append("  IP Address: " + inetAddress.getHostAddress() + "\n");
                }

                Enumeration<InetAddress> dnsAddresses = networkInterface.getInetAddresses();
                while (dnsAddresses.hasMoreElements()) {
                    InetAddress dnsAddress = dnsAddresses.nextElement();
                    textArea.append("  DNS Server: " + dnsAddress.getHostAddress() + "\n");
                }

                // You may need to retrieve gateway information based on your platform.
                // It might not be available through standard Java APIs.
                textArea.append("  Gateway: [Gateway Information Not Available]\n");

                textArea.append("\n");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            textArea.append("Error fetching network information.");
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Networking Info Panel");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.add(new NetworkingInfoPanel());
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }
}
