import java.util.List;

public interface IConnectionListModel<T> {

    public String getName();

    public List<T> listConnections();
}
