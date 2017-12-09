package buonanotte;

import buonanotte.model.Room;
import buonanotte.model.RoomType;
import buonanotte.view.AdminController;
import buonanotte.provider.DatabaseAdapter;
import buonanotte.view.OrderController;
import buonanotte.view.RootLayoutController;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BuonaNotte extends Application {
    
    private DatabaseAdapter adapter;
    private Stage privateStage;
    private BorderPane rootLayout;
    private RootLayoutController rootController;
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
        this.privateStage.setTitle("Admin Control Section");
        
        initRootLayout();
        
        FXMLLoader loader = new FXMLLoader();
        
        loader.setLocation(BuonaNotte.class.getResource("view/Admin.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        
        rootLayout.setCenter(root);
        
        adminController = loader.getController();
        adminController.setMainApp(this);
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
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BuonaNotte.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            rootController = loader.getController();
            rootController.setMainApp(this);
            Scene scene = new Scene(rootLayout);
            privateStage.setScene(scene);
            privateStage.show();
        } catch (IOException ex) {
            Logger.getLogger(BuonaNotte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetRoomAndRoomTypesCollection() {
        roomTypes.clear();
        roomData.clear();
        setRoomData();
    }
    
}
