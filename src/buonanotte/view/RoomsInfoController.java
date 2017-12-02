/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view;

import buonanotte.BuonaNotte;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author bilguun
 */
public class RoomsInfoController implements Initializable {
    
    private BuonaNotte mainapp;

    @FXML
    private TilePane MainTilePane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    public void test(){
        ResultSet rs = this.mainapp.getAdapder().query("rooms","roomNo","roomNo = roomNo");
        try {
            while(rs.next()) {
                MainTilePane.getChildren().add(createElement(rs.getInt("roomNo")));
            }
        } 
        catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
    }
    public void setMainApp(BuonaNotte mainApp){
        this.mainapp = mainApp;
        test();
    }
    private StackPane createElement(int number) {
        StackPane stack = new StackPane();
        Rectangle rectangle = new Rectangle(100, 100);
        rectangle.setStroke(Color.ORANGE);
        rectangle.setFill(Color.STEELBLUE);
        stack.getChildren().addAll(rectangle, new Text(""+number));
        stack.setOnMouseClicked((event) -> {
            mainapp.showOrderDialog(number);
        });
        return stack;
    }
}