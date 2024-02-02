import javax.swing.*;
import java.awt.*;

public class IpInputDialog extends JDialog {
    private IpInputPanel ipInputPanel;
    private String inputText = null;

    public IpInputDialog(Frame owner) {
        super(owner, "Enter IP Address", true);
        setupUI();
    }

    private void setupUI() {
        ipInputPanel = new IpInputPanel();
        setLayout(new BorderLayout());
        add(ipInputPanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            inputText = ipInputPanel.getIpAddress();
            if (!isValidIpAddress(inputText)) { // Assume isValidIpAddress is implemented elsewhere
                JOptionPane.showMessageDialog(this, "Invalid IP address. Please enter a valid IP.", "Invalid IP", JOptionPane.ERROR_MESSAGE);
                inputText = null; // Reset input text if invalid
                ipInputPanel.resetFields();
            } else {
                setVisible(false);
            }
        });
        add(okButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    public String showDialog() {
        setVisible(true);
        return inputText;
    }

    private boolean isValidIpAddress(String ipAddress) {
        String[] segments = ipAddress.split("\\.");
        if (segments.length != 4) {
            return false; // An IPv4 address must contain 4 segments.
        }
        for (String segment : segments) {
            try {
                int value = Integer.parseInt(segment);
                if (value < 0 || value > 255) {
                    return false; // Each segment must be within the range 0-255.
                }
                if (segment.length() > 1 && segment.startsWith("0")) {
                    return false; // Leading zeros are not allowed.
                }
            } catch (NumberFormatException e) {
                return false; // Each segment must be a valid integer.
            }
        }
        return true;
    }

}
