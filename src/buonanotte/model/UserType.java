/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.model;

/**
 *
 * @author archer
 */
public class UserType {
    
    private int id;
    private String type;
    
    public UserType(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public int getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
