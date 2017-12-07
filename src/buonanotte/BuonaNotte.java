package buonanotte;

import buonanotte.model.Room;
<<<<<<< HEAD
import buonanotte.model.RoomType;
import buonanotte.provider.DatabaseAdapter;
import buonanotte.view.AdminController;
=======
import buonanotte.provider.DatabaseAdapter;
import buonanotte.view.LoginController;
import buonanotte.view.OrderController;
import buonanotte.view.RoomsInfoController;
>>>>>>> 0c5dee2cbd4aaa32c225f62d59c08e3d24c068b9
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BuonaNotte extends Application {
    
    private DatabaseAdapter adapter;
    private Stage privateStage;
    private String currentUser;
    private AdminController adminController;
    
    private ObservableList<RoomType> roomTypes = FXCollections.observableArrayList();
    private ObservableList<Room> roomData = FXCollections.observableArrayList();
    
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        adapter = new DatabaseAdapter();
        setRoomData();
        currentUser = null;
        this.privateStage = stage;
        this.privateStage.setTitle("testing");
        FXMLLoader loader = new FXMLLoader();
<<<<<<< HEAD
        
        loader.setLocation(BuonaNotte.class.getResource("view/Admin.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        
        adminController = loader.getController();
        adminController.setMainApp(this);
        
=======
        loader.setLocation(BuonaNotte.class.getResource("view/RoomsInfo.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        RoomsInfoController controller = loader.getController();
        controller.setMainApp(this);
>>>>>>> 0c5dee2cbd4aaa32c225f62d59c08e3d24c068b9
        privateStage.setScene(new Scene(root));
        privateStage.setTitle("Hotel Project");
        privateStage.show();
        //showOrderDialog(1);
    }
    
    public void setUser(String username) {
        this.currentUser = username;
    }
    
    public DatabaseAdapter getAdapder() {
        return this.adapter;
    }
    
    public void setRoomData() {
        ResultSet rs = adapter.query("roomtypes", "*", "true");
        try {
            while(rs.next()) {
                roomTypes.add(new RoomType(rs.getInt("id"), rs.getString("roomtype"), rs.getDouble("price")));
            }
            
            rs = adapter.query("rooms", "*", "true");
            while(rs.next()) {
                int currentType = rs.getInt("typeid");
                int roomId = rs.getInt("roomno");
                roomTypes.forEach(type -> {
                    if(type.getId() == currentType) {
                        roomData.add(new Room(roomId, type));
                    }
                });
            }
            rs.close();
            
        } catch (SQLException ex) {
            System.out.println("Error occured while retrieving room data: " + ex.getMessage());
        }
    }
    
    public ObservableList<Room> getRoomData() {
        return this.roomData;
    }
    
    public ObservableList<RoomType> getRoomTypes() {
        return this.roomTypes;
    }

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
