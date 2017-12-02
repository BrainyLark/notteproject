/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author archer
 */
public class RoomType {
    private final IntegerProperty id;
    private final StringProperty type;
    private final DoubleProperty price;
    
    public RoomType(int no, String tp, double prc) {
        id = new SimpleIntegerProperty(no);
        type = new SimpleStringProperty(tp);
        price = new SimpleDoubleProperty(prc);
    }
    
    public int getId() {
        return id.get();
    }
    
    public void setId(int no) {
        id.set(no);
    }
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getType() {
        return type.get();
    }
    
    public void setType(String tp) {
        type.set(tp);
    }
    
    public StringProperty typeProperty() {
        return type;
    }
    
    public double getPrice() {
        return price.get();
    }
    
    public void setPrice(double prc) {
        price.set(prc);
    }
    
    public DoubleProperty priceProperty() {
        return price;
    }
    
}
