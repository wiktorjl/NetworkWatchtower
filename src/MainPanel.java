import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainPanel extends JPanel {
    private BlacklistPanel blacklistPanel = new BlacklistPanel();
    public MainPanel() {

        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Incoming", new ConnectionPanel("incoming.csv"));
        tabbedPane.addTab("Outgoing", new ConnectionPanel("outgoing.csv"));
        tabbedPane.addTab("Blacklist", blacklistPanel);
        tabbedPane.add("Network Info", new NetworkingInfoPanel());

        add(tabbedPane, BorderLayout.CENTER);

    }

}
