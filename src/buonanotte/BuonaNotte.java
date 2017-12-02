/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte;

import buonanotte.model.Room;
import buonanotte.provider.DatabaseAdapter;
import buonanotte.view.LoginController;
import buonanotte.view.OrderController;
import buonanotte.view.RoomsInfoController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
        this.privateStage.setTitle("testing");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BuonaNotte.class.getResource("view/RoomsInfo.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        RoomsInfoController controller = loader.getController();
        controller.setMainApp(this);
        privateStage.setScene(new Scene(root));
        privateStage.show();
        //showOrderDialog(1);
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
    public boolean showOrderDialog(int roomNumber) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BuonaNotte.class.getResource("view/Order.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Room order");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(privateStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            OrderController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setRoom(roomNumber);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
