package beans;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by matanghuy on 7/2/14.
 */
public class TransactionItem {
    private SimpleStringProperty name;
    private SimpleStringProperty comment;
    private SimpleDoubleProperty cost;
    private int id;


    public TransactionItem() {
        name = new SimpleStringProperty();
        comment = new SimpleStringProperty();
        cost = new SimpleDoubleProperty();

    }
    public TransactionItem(String name, double cost, String comment) {
        this();
        this.name.set(name);
        this.cost.set(cost);
        this.comment.set(comment);

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }
    public double getCost() {
        return cost.get();
    }
    public String getComment() {
        return comment.get();
    }
}