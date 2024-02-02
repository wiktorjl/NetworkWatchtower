import javax.swing.*;
import java.awt.*;
import java.io.File;

public class WatchtowerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Network Monitor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new MainPanel());
            frame.setLocationRelativeTo(null); // Center the window
            minimizeToSystemTray(frame);
            frame.setVisible(true);

            createMenuBar(frame);
        });
    }

    private static void minimizeToSystemTray(JFrame frame) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray not supported");
            return;
        }

        frame.addWindowStateListener(e -> {
            if (e.getNewState() == JFrame.ICONIFIED) {
                try {
                    final TrayIcon trayIcon = new TrayIcon(new ImageIcon("your_icon_path_here.png").getImage());
                    SystemTray.getSystemTray().add(trayIcon);
                    trayIcon.addActionListener(event -> {
                        SystemTray.getSystemTray().remove(trayIcon);
                        frame.setVisible(true);
                        frame.setState(JFrame.NORMAL);
                    });
                    frame.setVisible(false);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static void createMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load Blacklist");
        loadItem.addActionListener(e -> loadBlacklist());
        fileMenu.add(loadItem);

        JMenuItem saveItem = new JMenuItem("Save Blacklist");
        saveItem.addActionListener(e -> saveBlacklist());
        fileMenu.add(saveItem);

        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem usageItem = new JMenuItem("Usage");
        usageItem.addActionListener(e -> showUsageDialog());
        helpMenu.add(usageItem);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
    }

    private static void loadBlacklist() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Assuming BlacklistPanel has a method to load from a File
//            blacklistPanel.loadFromFile(file);
        }
    }

    private static void saveBlacklist() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Assuming BlacklistPanel has a method to save to a File
//            blacklistPanel.saveToFile(file);
        }
    }

    private static void showUsageDialog() {
        JOptionPane.showMessageDialog(null, "Usage Instructions:\n"
                + "- Load and save blacklist using the File menu.\n"
                + "- Add or remove IPs from the blacklist in the Blacklist tab.\n"
                + "- View incoming and outgoing connections in their respective tabs.", "Usage", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showAboutDialog() {
        JOptionPane.showMessageDialog(null, "NetGuardian Lite\nVersion 1.0\n"
                + "Developed by [Your Name or Organization].\n"
                + "For more information, visit [Your Website or Contact Info].", "About NetGuardian Lite", JOptionPane.INFORMATION_MESSAGE);
    }
}
