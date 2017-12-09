package buonanotte.view;

import buonanotte.BuonaNotte;
import buonanotte.view.report.ReportController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class RootLayoutController implements Initializable {
    
    @FXML
    MenuItem reportItem;
    @FXML
    MenuItem exitItem;
    @FXML
    MenuItem helpItem;
    private ReportController reportController;
    private BuonaNotte mainapp;
    
    public void initBtns() {
        reportItem.setOnAction((e) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("report/Report.fxml"));
                TabPane reportTabPane = (TabPane) loader.load();
                reportController = loader.getController();
                reportController.setMainApp(mainapp);
                Scene scene = new Scene(reportTabPane);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Тайлангийн булан");
                stage.show();
            } catch (IOException ex) {
                System.out.println("Error occured while reading report tab pane: " + ex.getMessage());
            }
        });
        
        exitItem.setOnAction((e) -> {
            System.out.println("Exit item clicked");
        });
        
        helpItem.setOnAction((e) -> {
            System.out.println("Help item clicked");
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initBtns();
    }  
    
    public void setMainApp(BuonaNotte app) {
        mainapp = app;
    }
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }
    
}
