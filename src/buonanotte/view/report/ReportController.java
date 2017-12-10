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
    private IncomeUsageChartController incomeController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabs();
    }

    public void initTabs() {
        roomUseTab.setContent(readFxmlLayout("RoomUsageChart.fxml", 1));
        incomeUseTab.setContent(readFxmlLayout("IncomeUsageChart.fxml", 2));
    }
    
    public AnchorPane readFxmlLayout(String fxmlName, int tabNo) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlName));
            AnchorPane pane = (AnchorPane) loader.load();
            switch(tabNo) {
                case 1:
                    roomUsageController = loader.getController();
                    break;
                case 2:
                    incomeController = loader.getController();
                    break;
            }
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
        incomeController.setMainApp(mainapp);
    }
    
}
