import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
public class BlacklistPanel extends JPanel {
    private JList<String> blacklist;
    private DefaultListModel<String> model;

    public BlacklistPanel() {
        setLayout(new BorderLayout());
        model = new DefaultListModel<>();
        blacklist = new JList<>(model);
        loadBlacklist();
        add(new JScrollPane(blacklist), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Add IP");
        JButton removeButton = new JButton("Remove IP");
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            IpInputDialog dialog = new IpInputDialog(JOptionPane.getFrameForComponent(BlacklistPanel.this));
            String newIp = dialog.showDialog();
            if (newIp != null && !newIp.isEmpty() && isValidIpAddress(newIp)) {
                model.addElement(newIp);
                saveBlacklist();
            } else if (newIp != null) {
                JOptionPane.showMessageDialog(BlacklistPanel.this, "Invalid IP address. Please enter a valid IP.", "Invalid IP", JOptionPane.ERROR_MESSAGE);
            }
        });


        removeButton.addActionListener(e -> {
            int selectedIndex = blacklist.getSelectedIndex();
            if (selectedIndex != -1) {
                model.remove(selectedIndex);
                saveBlacklist();
            } else {
                JOptionPane.showMessageDialog(BlacklistPanel.this, "Please select an IP to remove.");
            }
        });


    }

    private void loadBlacklist() {
        File file = new File("blacklist.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                // Since saveBlacklist saves as a direct JSON array, we read it as such.
                Type type = new TypeToken<List<String>>() {}.getType();
                List<String> blockedIps = gson.fromJson(reader, type);
                // Ensure the model is clear before loading to prevent duplicates
                model.clear();
                for (String ip : blockedIps) {
                    model.addElement(ip);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading blacklist: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void saveBlacklist() {
        List<String> blockedIps = Collections.list(model.elements());
        try (Writer writer = new FileWriter("blacklist.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(blockedIps, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving blacklist: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidIpAddress(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return true;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    public void loadFromFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<List<String>>() {}.getType();
            List<String> ips = gson.fromJson(reader, type);
            model.clear();
            ips.forEach(model::addElement);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load blacklist from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            List<String> ips = Collections.list(model.elements());
            gson.toJson(ips, writer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to save blacklist to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
