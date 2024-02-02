import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Vector;

public class ConnectionPanel extends JPanel {
    public ConnectionPanel(String csvFile) {
        setLayout(new BorderLayout());
        String[] columnNames = {"IP Address", "Port", "Count of Connections", "First Seen", "Last Seen", "Hostname"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        // Populate the model with data from CSV
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(new JScrollPane(table), BorderLayout.CENTER);
        addTablePopupMenu(table);
    }


    private void addTablePopupMenu(JTable table) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem blockItem = new JMenuItem("Block IP");
        blockItem.addActionListener(e -> {
            blockIp(table);
        });
        popupMenu.add(blockItem);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    private void blockIp(JTable table) {
        int row = table.getSelectedRow();
        if (row != -1) { // Make sure a row is selected
            String ip = (String) table.getValueAt(row, 0); // Assuming IP Address is in the first column
            // Implement the logic to add the selected IP to the blacklist
            // For example, update the blacklist JSON and refresh the BlacklistPanel if necessary
            System.out.println("Blocking IP: " + ip); // Placeholder for actual block operation
        }
    }

}
