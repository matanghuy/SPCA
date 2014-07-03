package beans;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import spca.datalayer.DataRow;

/**
 * Created by matanghuy on 7/2/14.
 */
public class TransactionType {
    private SimpleStringProperty name;
    private SimpleIntegerProperty id;

    public TransactionType() {
        name = new SimpleStringProperty();
        id = new SimpleIntegerProperty();
    }
    public TransactionType(DataRow typeRow) {
        this();
        setName((String)typeRow.getObject("Name"));
        setId((Integer)typeRow.getObject("ID"));
    }
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
