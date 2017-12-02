/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author archer
 */
public class Room {
    private final IntegerProperty number;
    private final ObjectProperty<RoomType> type;
    
    public Room(int num, RoomType rType) {
        number = new SimpleIntegerProperty(num);
        type = new SimpleObjectProperty<>(rType);
    }
    
    public int getNumber() {
        return number.get();
    }
    
    public void setNumber(int num) {
        number.set(num);
    }
    
    public IntegerProperty numberProperty() {
        return number;
    }
    
    public RoomType getType() {
        return type.get();
    }
    
    public void setType(RoomType tp) {
        type.set(tp);
    }
    
    public ObjectProperty<RoomType> typeProperty() {
        return type;
    }
}
