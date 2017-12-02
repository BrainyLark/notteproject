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
public class User {
    private String username;
    private UserType type;
    
    public User(String username, UserType type) {
        this.username = username;
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public UserType getType() {
        return type;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setType(UserType type) {
        this.type = type;
    }
}
