/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view;

import buonanotte.BuonaNotte;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author archer
 */
public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private RadioButton adminChecker;
    @FXML
    private Button submitBtn;
    
    private BuonaNotte mainapp;
    
    public boolean isValidUser(String user, String pass) {
        ResultSet rs = this.mainapp.getAdapder().query("users", "id", "username='" + user + "' and passwordtoken='" + pass + "'");
        try {
            if(rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
        return false;
    }
    
    public void giveWarning(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        ButtonType alrightBtn = new ButtonType("Got it.");
        alert.getButtonTypes().setAll(alrightBtn);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void setSync() {
        submitBtn.setOnAction((e) -> {
            String user = username.getText();
            String pass = password.getText();
            if(isValidUser(user, pass)) {
                this.mainapp.setUser(user);
            } else {
                giveWarning("This is not valid user!", "The account you are trying to use isn't acceptable! We suggest you to check if you have inserted the right username or password.");
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setSync();
    }    
    
    public void setMainApp(BuonaNotte app) {
        this.mainapp = app;
    }
    
}
