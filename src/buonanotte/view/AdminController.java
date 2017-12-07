package buonanotte.view;

import buonanotte.BuonaNotte;
import buonanotte.model.Room;
import buonanotte.model.RoomType;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AdminController implements Initializable {
    @FXML
    private TreeView<Integer> roomList;
    @FXML
    private Label roomNoLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private Label roomPriceLabel;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button createBtn;
    @FXML
    private TableView<RoomType> typeTable;
    @FXML
    private TableColumn<RoomType, Number> typeIdColumn;
    @FXML
    private TableColumn<RoomType, String> typeNameColumn;
    @FXML
    private TableColumn<RoomType, Number> typePriceColumn;
    @FXML
    private Label guestLabel;
    @FXML
    private Label checkoutLabel;
    @FXML
    private Label billLabel;
    
    private int currentRoomNo;
    
    private BuonaNotte mainapp;
    
    public void showRoomDetails() throws SQLException {
        if(currentRoomNo != 0) {
            Room room = null;
            room = mainapp.getRoomData().filtered(r -> r.getNumber()==currentRoomNo).get(0);
            roomNoLabel.setText(Integer.toString(room.getNumber()));
            roomTypeLabel.setText(room.getType().getType());
            roomPriceLabel.setText(Double.toString(room.getType().getPrice()));
            showOrderDetails();
        } else {
            roomNoLabel.setText("");
            roomTypeLabel.setText("");
            roomPriceLabel.setText("");
        }
    }
    
    public void initTree() {
        TreeItem rootItem = new TreeItem("All Rooms");
        
        ArrayList<TreeItem> roomItems = new ArrayList();
        
        mainapp.getRoomData().forEach(room -> roomItems.add(new TreeItem(room.getNumber())));
        
        rootItem.getChildren().addAll(roomItems);
        roomList.setRoot(rootItem);
        
        roomList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(observable.getValue().getParent() != null) {
                currentRoomNo = observable.getValue().getValue();
                System.out.println(mainapp.getRoomData().filtered(r -> r.getNumber() == currentRoomNo).get(0).getNumber());
                try {
                    showRoomDetails();
                } catch (SQLException ex) {
                    
                }
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentRoomNo = 0;
        try {
            showRoomDetails();
        } catch (SQLException ex) {
            
        }
        initButtons();
        initTable();
    }

    public void setMainApp(BuonaNotte app) {
        this.mainapp = app;
        typeTable.setItems(mainapp.getRoomTypes());
        initTree();
    }
    
    public void initTable() {
        typeIdColumn.setCellValueFactory(celldata -> celldata.getValue().idProperty());
        typeNameColumn.setCellValueFactory(celldata -> celldata.getValue().typeProperty());
        typePriceColumn.setCellValueFactory(celldata -> celldata.getValue().priceProperty());
    }
    
    public void initButtons() {
        deleteBtn.setOnAction((e) -> {
            if(currentRoomNo > 0) {
                if(mainapp.getAdapder().delete("rooms", "roomno = " + currentRoomNo)) {
                    mainapp.getRoomData().removeIf((r) -> r.getNumber() == currentRoomNo);
                    currentRoomNo = 0;
                    initTree();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setHeaderText(currentRoomNo + " дугаартай өрөөний мэдээллийг устгалаа!");
                    alert.setTitle("Амжилттай устгагдлаа!");
                    alert.showAndWait();
                }
            }
        });
        
        createBtn.setOnAction((e) -> {
            
        });
    }   

    private void showOrderDetails() throws SQLException {
        String gid = "[байхгүй]";
        String date = "[байхгүй]";
        String bill = "[байхгүй]";
        
        ResultSet rs = null;
        rs = mainapp.getAdapder().query("orders", "*", "checkout > '" + LocalDate.now() + "' and roomid = " + currentRoomNo);
        
        if(rs.next()) {
            gid = Integer.toString(rs.getInt("guestid"));
            date = rs.getDate("checkout").toString();
            bill = Double.toString(rs.getDouble("totalbill"));
        }
        
        guestLabel.setText(gid);
        checkoutLabel.setText(date);
        billLabel.setText(bill);
    }
    
}
