package buonanotte.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RootLayoutController implements Initializable {
    
    @FXML
    MenuItem reportItem;
    @FXML
    MenuItem exitItem;
    @FXML
    MenuItem helpItem;
    
    public void initBtns() {
        reportItem.setOnAction((e) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("report/Report.fxml"));
                TabPane reportTabPane = (TabPane) loader.load();
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
    
}
