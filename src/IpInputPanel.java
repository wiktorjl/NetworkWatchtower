import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IpInputPanel extends JPanel {
    private final JTextField[] segmentFields = new JTextField[4];

    public IpInputPanel() {
        setLayout(new FlowLayout());
        initSegmentFields();
    }

    private void initSegmentFields() {
        for (int i = 0; i < segmentFields.length; i++) {
            JTextField field = new JTextField(3);
            segmentFields[i] = field;
            add(field);
            if (i < segmentFields.length - 1) {
                add(new JLabel("."));
            }

            int index = i;
            field.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == '.' || field.getText().length() == 2) {
                        if (index + 1 < segmentFields.length) {
                            segmentFields[index + 1].requestFocus();
                        }
                    }
                }
            });
        }
    }

    public String getIpAddress() {
        StringBuilder ipAddress = new StringBuilder();
        for (int i = 0; i < segmentFields.length; i++) {
            ipAddress.append(segmentFields[i].getText());
            if (i < segmentFields.length - 1) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

    public void resetFields() {
        for (JTextField field : segmentFields) {
            field.setText("");
        }
        segmentFields[0].requestFocus();
    }
}
