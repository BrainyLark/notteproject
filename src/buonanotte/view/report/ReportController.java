/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view.report;

import buonanotte.view.RootLayoutController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author archer
 */
public class ReportController implements Initializable {

    private RootLayoutController parentController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setParentController(RootLayoutController parentController) {
        this.parentController = parentController;
    }
    
    public RootLayoutController getParentController() {
        return parentController;
    }
    
}
