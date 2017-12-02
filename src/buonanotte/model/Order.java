/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author archer
 */
public class Order {
    private final IntegerProperty id;
    private final StringProperty checkin;
    private final StringProperty checkout;
    private final DoubleProperty totalBill;
    private final ObjectProperty<Room> room;
    private final ObjectProperty<Guest> guest;
    
    public Order(int id, String checkin, String checkout, double bill, Room room, Guest guest) {
        this.id = new SimpleIntegerProperty(id);
        this.checkin = new SimpleStringProperty(checkin);
        this.checkout = new SimpleStringProperty(checkout);
        this.totalBill = new SimpleDoubleProperty(bill);
        this.room = new SimpleObjectProperty<>(room);
        this.guest = new SimpleObjectProperty<>(guest);
    }
    
    public void setId(int id) {
        this.id.set(id);
    }
    
    public int getId() {
        return id.get();
    }
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    public void setCheckin(String checkin) {
        this.checkin.set(checkin);
    }
    
    public String getCheckin() {
        return checkin.get();
    }
    
    public StringProperty checkinProperty() {
        return checkin;
    }
    
    public void setCheckout(String checkout) {
        this.checkout.set(checkout);
    }
    
    public String getCheckout() {
        return checkout.get();
    }
    
    public StringProperty checkoutProperty() {
        return checkout;
    }
    
     public void setTotalBill(double totalBill) {
        this.totalBill.set(totalBill);
    }
    
    public double getTotalBill() {
        return totalBill.get();
    }
    
    public DoubleProperty totalBillProperty() {
        return totalBill;
    }
    
    public void setRoom(Room room) {
        this.room.set(room);
    }
    
    public Room getRoom() {
        return this.room.get();
    }
    
    public ObjectProperty<Room> roomProperty() {
        return room;
    }
    
    public void setGuest(Guest guest) {
        this.guest.set(guest);
    }
    
    public Guest getGuest() {
        return this.guest.get();
    }
    
    public ObjectProperty<Guest> guestProperty() {
        return guest;
    }
    
}
