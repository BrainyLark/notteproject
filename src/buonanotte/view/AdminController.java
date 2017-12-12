package buonanotte.view;

import buonanotte.BuonaNotte;
import buonanotte.model.Room;
import buonanotte.model.RoomType;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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
    private Button typeUpdateBtn;
    @FXML
    private Button typeDeleteBtn;
    @FXML
    private Button typeCreateBtn;
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
        if (currentRoomNo != 0) {
            Room room = null;
            room = mainapp.getRoomData().filtered(r -> r.getNumber() == currentRoomNo).get(0);
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
        TreeItem rootItem = new TreeItem("Бүх Өрөөнүүд");

        ArrayList<TreeItem> roomItems = new ArrayList();

        mainapp.getRoomData().forEach(room -> roomItems.add(new TreeItem(room.getNumber())));

        rootItem.getChildren().addAll(roomItems);
        roomList.setRoot(rootItem);
    }

    public void initTreeListener() {
        roomList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {    
                if (newValue.getValue() != null) {
                    currentRoomNo = newValue.getValue();
                    //System.out.println(mainapp.getRoomData().filtered(r -> r.getNumber() == currentRoomNo).get(0).getNumber());
                    try {
                        showRoomDetails();
                    } catch (SQLException ex) {

                    }
                }
            } catch(NumberFormatException | ClassCastException | NullPointerException ex) {
                System.out.println("Not a Number or Class Cast Exception or Null Pointer Exception!");
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
        initTreeListener();
    }

    public void setMainApp(BuonaNotte app) {
        this.mainapp = app;
        typeTable.setItems(mainapp.getRoomTypes());
        initTree();
    }
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }

    public void initTable() {
        typeIdColumn.setCellValueFactory(celldata -> celldata.getValue().idProperty());
        typeNameColumn.setCellValueFactory(celldata -> celldata.getValue().typeProperty());
        typePriceColumn.setCellValueFactory(celldata -> celldata.getValue().priceProperty());
    }

    public void initButtons() {
        deleteBtn.setOnAction((e) -> {
            if (currentRoomNo > 0) {
                if (mainapp.getAdapder().delete("rooms", "roomno = " + currentRoomNo)) {
                    mainapp.getRoomData().removeIf((r) -> r.getNumber() == currentRoomNo);
                    int temp = currentRoomNo;
                    currentRoomNo = 0;
                    initTree();
                    showAlert(AlertType.INFORMATION, "Амжилттай устгагдлаа!", temp + " дугаартай өрөөний мэдээллийг устгалаа!");
                }
            }
        });

        createBtn.setOnAction((e) -> {
            createRoom();
        });

        updateBtn.setOnAction((e) -> {
            //createRoom(2);
        });

        typeCreateBtn.setOnAction((e) -> {
            createType();
        });
        
        typeDeleteBtn.setOnAction((e) -> {
            deleteType();
        });
    }

    private void showOrderDetails() throws SQLException {
        String gid = "[байхгүй]";
        String date = "[байхгүй]";
        String bill = "[байхгүй]";

        ResultSet rs = null;
        rs = mainapp.getAdapder().query("orders", "*", "checkout > '" + LocalDate.now() + "' and roomid = " + currentRoomNo);

        if (rs.next()) {
            gid = Integer.toString(rs.getInt("guestid"));
            date = rs.getDate("checkout").toString();
            bill = Double.toString(rs.getDouble("totalbill"));
        }

        guestLabel.setText(gid);
        checkoutLabel.setText(date);
        billLabel.setText(bill);
    }

    public void showAlert(AlertType type, String title, String body) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(body);
        alert.showAndWait();
    }

    private void createRoom() {
        Optional<Pair<String, String>> result = createCustomDialog("Өрөө үүсгэх", "Шинэ өрөө үүсгэх булан", "Дугаар:", "Төрлийн дугаар:");

        result.ifPresent(room -> {
            if (mainapp.getAdapder().insert("rooms", room.getKey() + ", " + room.getValue())) {
                mainapp.resetRoomAndRoomTypesCollection();
                initTree();
                showAlert(AlertType.CONFIRMATION, "Баталгаажуулалт", "Үйлдэл амжилттай хийгдлээ");
            } else {
                showAlert(AlertType.ERROR, "Алдаа", "Өрөө үүсгэхэд алдаа гарлаа. Буруу өгөгдөл оруулсан байна. Өгөгдлөө нягтлаад дахин оролдоно уу.");
            }
        });
    }

    public Optional<Pair<String, String>> createCustomDialog(String title, String body, String fLabel, String sLabel) {
        Dialog<Pair<String, String>> dialog = new Dialog();
        dialog.setTitle(title);
        dialog.setHeaderText(body);
        ButtonType sendBtnType = new ButtonType("Илгээх", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendBtnType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstField = new TextField();
        TextField secondField = new TextField();
        grid.add(new Label(fLabel), 0, 0);
        grid.add(firstField, 1, 0);
        grid.add(new Label(sLabel), 0, 1);
        grid.add(secondField, 1, 1);

        Node btn = dialog.getDialogPane().lookupButton(sendBtnType);
        btn.setDisable(true);
        firstField.textProperty().addListener((observable, oldVal, newVal) -> {
            btn.setDisable(newVal.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> firstField.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendBtnType) {
                return new Pair<>(firstField.getText(), secondField.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        return result;
    }

    public void createType() {
        Optional<Pair<String, String>> result = createCustomDialog("Төрөл үүсгэх", "Төрөл үүсгэх зориулалттай булан", "Төрлийн нэр:", "Төрлийн үнэ");

        result.ifPresent(type -> {
            if (mainapp.getAdapder().insert("roomtypes", "'" + type.getKey() + "', " + type.getValue())) {
                mainapp.resetRoomAndRoomTypesCollection();
                showAlert(AlertType.CONFIRMATION, "Баталгаажуулалт", "Үйлдэл амжилттай хийгдлээ");
            } else {
                showAlert(AlertType.ERROR, "Алдаа", "Үйлдэл амжилтгүй боллоо");
            }
        });
    }
    
    public void deleteType() {
        int selectedType = typeTable.getSelectionModel().getSelectedItem().getId();
        if(mainapp.getAdapder().delete("roomtypes", "id = " + selectedType)) {
            mainapp.getRoomTypes().removeIf(e -> e.getId() == selectedType);
            showAlert(AlertType.CONFIRMATION, "Баталгаажуулалт", "Төрөл амжилттай устгагдлаа");
        } else {
            showAlert(AlertType.ERROR, "Алдаа", "Төрөл устгагдахад алдаа гарлаа");
        }
    }

}
