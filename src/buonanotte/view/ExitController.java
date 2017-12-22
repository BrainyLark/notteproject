/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view;

import buonanotte.BuonaNotte;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bilguun
 */
public class ExitController implements Initializable {
    @FXML
    private Label FirstNameLabel,LastNameLabel,RegistryNumberLabel;
    @FXML
    private Label RoomNumLabel,RoomTypeLabel,StartDateLabel,EndDateLabel,PaidLabel,ReturnLabel,RoomStayedField,RoomPriceField;
    private BuonaNotte mainApp;
    private Stage dialogStage;
    private int order;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void handleOk(){
        
    }
    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    public void setMainApp(BuonaNotte aThis) {
        mainApp = aThis;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrder(int orderNum) {
        order = orderNum;
        Date start,end;  
        ResultSet rs = this.mainApp.getAdapder().query("orders,guests,rooms,roomtypes", "*", "orders.id=" + order +" and guestid = guests.id and roomid = roomNo and typeid = roomtypes.id");
        try {
            if (rs.next()) {
                //user info fill
                FirstNameLabel.setText(rs.getString("firstname"));
                LastNameLabel.setText(rs.getString("lastname"));
                RegistryNumberLabel.setText(rs.getString("registerid"));
                RoomNumLabel.setText(rs.getString("roomid"));
                RoomTypeLabel.setText(rs.getString("roomtype"));
                PaidLabel.setText(rs.getString("totalbill"));
                start = rs.getDate("checkin");
                StartDateLabel.setText(rs.getString("checkin"));
                end = rs.getDate("checkout");
                EndDateLabel.setText(rs.getString("checkout"));
                int day = Period.between(start.toLocalDate(),LocalDate.now()).getDays();
                int ret = rs.getInt("price");
                int temp = rs.getInt("totalbill");
                if(day == 0) day++;
                RoomStayedField.setText(""+day);
                RoomPriceField.setText(""+ret);
                ReturnLabel.setText(""+(temp-day*ret));
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
            return;
        }
    }
    
}
