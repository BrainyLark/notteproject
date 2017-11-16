/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view;

import buonanotte.BuonaNotte;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    
    public void setSync() {
        submitBtn.setOnAction((e) -> {
            //some good function when button clicked
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
