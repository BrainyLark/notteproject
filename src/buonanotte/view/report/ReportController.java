/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view.report;

import buonanotte.BuonaNotte;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class ReportController implements Initializable {

    @FXML
    private Tab roomUseTab;
    @FXML
    private Tab incomeUseTab;
    @FXML
    private Tab totalRoomTab;
    @FXML
    private Tab userDataTab;

    private BuonaNotte mainapp;
    private RoomUsageChartController roomUsageController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabs();
    }

    public void initTabs() {
        roomUseTab.setContent(readFxmlLayout("RoomUsageChart.fxml"));
    }
    
    public AnchorPane readFxmlLayout(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlName));
            AnchorPane pane = (AnchorPane) loader.load();
            roomUsageController = loader.getController();
            return pane;
        } catch (IOException ex) {
            System.out.println("Error at reading fxml layout: " + ex.getMessage());
        }
        return null;
    }
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }
    
    public void setMainApp(BuonaNotte app) {
        mainapp = app;
        roomUsageController.setMainApp(mainapp);
    }
    
}
