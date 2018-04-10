package com.petworq.petworq;

/**
 * Created by charlietuttle on 4/9/18.
 */

public class DataObject {

    private int intProperty;
    private double doubleProperty;
    private String stringProperty;
    private long longProperty;

    // Generic constructor
    public DataObject() {}

    // Setter methods
    public void setIntProperty(int intProperty) { this.intProperty = intProperty; }
    public void setDoubleProperty(double doubleProperty) { this.doubleProperty = doubleProperty; }
    public void setStringProperty (String stringProperty) { this.stringProperty = stringProperty; }
    public void setLongProperty (Long longProperty) { this.longProperty = longProperty; }

    // Getter methods
    public int getIntProperty() { return this.intProperty; }
    public double getDoubleProperty() { return this.doubleProperty; }
    public String getStringProperty() { return this.stringProperty; }
    public long getLongProperty() { return this.longProperty; }
}
