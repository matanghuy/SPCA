package beans;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by matanghuy on 7/3/14.
 */
public class Animal {
    private SimpleStringProperty name;
    private SimpleStringProperty chipNumber;
    private SimpleStringProperty type;
    private SimpleStringProperty sourceCity;
    private int id;

    public Animal() {
        name = new SimpleStringProperty();
        chipNumber = new SimpleStringProperty();
        type = new SimpleStringProperty();
        sourceCity = new SimpleStringProperty();

    }

    public Animal(String name, String chipNumber, String type, String sourceCity, int id) {
        this();
        setName(name);
        setChipNumber(chipNumber);
        setSourceCity(sourceCity);
        setType(type);
        this.id = id;
    }
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getChipNumber() {
        return chipNumber.get();
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber.set(chipNumber);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getSourceCity() {
        return sourceCity.get();
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity.set(sourceCity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name=" + name +
                ", chipNumber=" + chipNumber +
                ", type=" + type +
                ", sourceCity=" + sourceCity +
                ", id=" + id +
                '}';
    }
}
