import java.net.Inet4Address;

public class MonitoredConnection {

    protected Inet4Address address;

    protected MonitoredConnectionType connectionType;


    public Inet4Address getAddress() {
        return address;
    }

    public void setAddress(Inet4Address address) {
        this.address = address;
    }

    public MonitoredConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(MonitoredConnectionType connectionType) {
        this.connectionType = connectionType;
    }
}
