/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view.report;

import buonanotte.BuonaNotte;
import buonanotte.model.Room;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author archer
 */
public class RoomUsageChartController implements Initializable {

    @FXML
    TableView<Room> roomTable;
    @FXML
    TableColumn<Room, Number> roomColumn;
    @FXML
    TableColumn<Room, String> typeColumn;
    @FXML
    Button button;
    
    
    private BuonaNotte mainapp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        button.setOnAction((e) -> {
            System.out.println(roomTable.getSelectionModel().getSelectedItem().getType().getType());
        });
    }
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }
    
    public void setMainApp(BuonaNotte app) {
        mainapp = app;
        roomTable.setItems(getMainApp().getRoomData());
        roomColumn.setCellValueFactory(celldata -> celldata.getValue().numberProperty());
        typeColumn.setCellValueFactory(celldata -> celldata.getValue().getType().typeProperty());
    }
    
}
