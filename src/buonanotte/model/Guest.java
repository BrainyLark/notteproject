/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author archer
 */
public class Guest {
    
    private final IntegerProperty guestId;
    private final StringProperty fullname;
    private final StringProperty registerId;
    private final StringProperty cardNumber;
    private final StringProperty country;
    
    public Guest(int id, String name, String regId, String cardNo, String country) {
        this.guestId = new SimpleIntegerProperty(id);
        this.fullname = new SimpleStringProperty(name);
        this.registerId = new SimpleStringProperty(regId);
        this.cardNumber = new SimpleStringProperty(cardNo);
        this.country = new SimpleStringProperty(country);
    }
    
    public int getGuestId() {
        return guestId.get();
    }
    
    public void setGuestId(int id) {
        guestId.set(id);
    }
    
    public IntegerProperty guestIdProperty() {
        return guestId;
    }
    
    public String getFullname() {
        return fullname.get();
    }
    
    public void setFullname(String fname) {
        fullname.set(fname);
    }
    
    public StringProperty fullnameProperty() {
        return fullname;
    }
    
    public String getRegisterId() {
        return registerId.get();
    }
    
    public void setRegisterId(String regNo) {
        registerId.set(regNo);
    }
    
    public StringProperty registerIdProperty() {
        return registerId;
    }
    
    public String getCardNumber() {
        return cardNumber.get();
    }
    
    public void setCardNumber(String cardNo) {
        cardNumber.set(cardNo);
    }
    
    public StringProperty cardNumberProperty() {
        return cardNumber;
    }
    
    public String getCountry() {
        return country.get();
    }
    
    public void setCountry(String cty) {
        country.set(cty);
    }
    
    public StringProperty countryProperty() {
        return country;
    } 
    
}
