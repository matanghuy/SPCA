package beans;

import spca.datalayer.DataRow;

/**
 * Created by matanghuy on 7/2/14.
 */
public class PaymentType {
    String name;
    int id;

    public PaymentType() {
    }

    public PaymentType(DataRow row) {
        setName((String)row.getObject("Name"));
        setId((int)row.getObject("Id"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
