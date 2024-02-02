import java.util.List;

public class Blacklist {
    private List<String> blocked_ips;

    // Getter and setter
    public List<String> getBlockedIps() {
        return blocked_ips;
    }

    public void setBlockedIps(List<String> blocked_ips) {
        this.blocked_ips = blocked_ips;
    }
}
