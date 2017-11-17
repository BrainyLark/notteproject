/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte;

import buonanotte.provider.DatabaseAdapter;
import buonanotte.view.LoginController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author archer
 */
public class BuonaNotte extends Application {
    
    private DatabaseAdapter adapter;
    private Stage privateStage;
    private String currentUser;
    
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        adapter = new DatabaseAdapter();
        currentUser = null;
        
        this.privateStage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BuonaNotte.class.getResource("view/Login.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        
        LoginController controller = loader.getController();
        controller.setMainApp(this);
        
        privateStage.setScene(new Scene(root));
        privateStage.show();
    }
    
    public void setUser(String username) {
        this.currentUser = username;
    }
    
    public DatabaseAdapter getAdapder() {
        return this.adapter;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
