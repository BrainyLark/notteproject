/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view;

/**
 *
 * @author bilguun
 */
import buonanotte.BuonaNotte;
import buonanotte.model.Guest;
import buonanotte.model.Order;
import buonanotte.model.Room;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Callback;

public class OrderController implements Initializable {

    @FXML
    private ComboBox roomNumField;
    @FXML
    private Label roomTypeField;
    @FXML
    private Label roomPriceField;
    @FXML
    private DatePicker roomStartDateField;
    @FXML
    private DatePicker roomEndDateField;
    @FXML
    private Label totalPriceField;
    @FXML
    private TextField FirstNameField;
    @FXML
    private TextField LastNameField;
    @FXML
    private TextField RegistryField;
    @FXML
    private TextArea OthersField;
    @FXML
    private Label ErrorField;
    @FXML
    private ListView OrderListView;
    @FXML
    private Button orderBtn;
    @FXML
    private Button ListEditBtn;
    @FXML
    private Button ListCancelBtn;
    @FXML
    private VBox OrdersSection;
    @FXML
    private TextField PaymentField;
    @FXML
    private Label ReturnValueField;

    private BuonaNotte mainApp;
    private int roomNum = -1;
    private Stage dialogStage;
    private String roomType;
    private int roomPrice;
    private boolean saveable = true;
    private boolean ordersDisabled = false;

    /**
     * The constructor
     *
     */
    public OrderController() {

    }

    public void disableOrdersPanel() {
        OrdersSection.setDisable(true);
        ordersDisabled = true;
    }

    public void setMainApp(BuonaNotte mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRoom(int roomNumber) {
        this.roomNum = roomNumber;
        ResultSet rs = this.mainApp.getAdapder().query("rooms, roomtypes", "roomtype,price", "roomNo=" + roomNumber + " and typeid = id");
        try {
            if (rs.next()) {
                this.roomType = rs.getString("roomtype");
                this.roomPrice = rs.getInt("price");
                this.roomNumField.getItems().add(new Text("" + this.roomNum));
                this.roomNumField.getSelectionModel().select(0);
                this.roomNumField.setDisable(true);
                this.roomPriceField.setText("" + this.roomPrice);
                this.roomTypeField.setText(this.roomType);
                this.roomStartDateField.setValue(LocalDate.now());
                this.roomEndDateField.setValue(LocalDate.now());
                this.totalPriceField.setText("" + this.roomPrice);
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
        if (ordersDisabled == false) {
            showRoomOrders();
        }
    }

    public boolean isOkClicked() {
        return true;
    }

    @FXML
    private void handleOk() {
        String registry = RegistryField.getText();
        String regPatt = "[ФЦУЖЭНГШҮЗКЪЩЕЙЫБӨАХРОЛДПЯЧЁСМИТЬВЮфцужэнгшүззкъщепдлорхаөбыйячёсмитьвю]{2}\\d{2}([0-1]{1}[0-9]{1})([0-3]{1}[0-9]{1})\\d{2}";
        Pattern rp = Pattern.compile(regPatt);
        Matcher m = rp.matcher(registry);
        if (registry.length() != 10) {
            ErrorField.setText("Регистерийн дугаар буруу");
            return;
        }
        int sar, udur;
        if (m.find()) {
            sar = Integer.parseInt(m.group(2));
            udur = Integer.parseInt(m.group(2));
        } else {
            ErrorField.setText("Регистерийн дугаар буруу");
            return;
        }
        if (sar > 12 || udur > 31) {
            ErrorField.setText("Регистерийн дугаар буруу");
            return;
        }
        int userid = 0;
        if (FirstNameField.getText().length() > 1 && LastNameField.getText().length() > 1 && saveable == true) {
            ResultSet rs = this.mainApp.getAdapder().query("guests", "id", "registerid = '" + RegistryField.getText().toUpperCase()+"'");
            try {
                if (rs.next()) {
                    userid = rs.getInt("id");
                } else {
                    boolean tmp = this.mainApp.getAdapder().insert("guests","'"+RegistryField.getText().toUpperCase() + "','" + FirstNameField.getText()+ "','" + LastNameField.getText()+"'");
                    if (tmp) {
                        System.out.println("Хадгалагдлаа");
                        rs = this.mainApp.getAdapder().query("guests", "id", "registerid ='" + RegistryField.getText().toUpperCase()+"'");
                        try{
                            if(rs.next()){
                                userid = rs.getInt("id");
                            }
                        }
                        catch (SQLException ex) {
                            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("Алдаа гарлаа");
                        return;
                    }
                }
            } catch (SQLException ex) {
                System.out.println("The error has been occured while querying user data: " + ex.getMessage());
            }
            ResultSet tmpRs = mainApp.getAdapder().query("orders","roomid,checkin,checkout","('"+roomStartDateField.getValue()+"' between checkin and checkout OR '"+roomEndDateField.getValue()+"' between checkin and checkout) and roomid = "+roomNum);
            try{
                if(tmpRs.next()){
                    ErrorField.setText("Тухайн он сар дээр захиалгатай байна");
                    return;
                }
            }
            catch (SQLException ex) {
                System.out.println("The error has been occured while querying user data: " + ex.getMessage());
            }
            boolean a = false;
            a = this.mainApp.getAdapder().insert("orders", "'" + roomStartDateField.getValue() + "','" + roomEndDateField.getValue() + "'," + totalPriceField.getText() + "," + roomNum + ","+userid);
            if (a) {
                ErrorField.setText("Хадгалагдлаа");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialogStage.close();
            } else {
                ErrorField.setText("Алдаа гарлаа");
                return;
            }
        } else {
            ErrorField.setText("Нэр оруулаагүй байна");
            return;
        }

    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void roomNumChanged() {
        String roomnumber = ((Text) roomNumField.getSelectionModel().getSelectedItem()).getText();
        roomNum = Integer.parseInt(roomnumber);
        ResultSet rs = this.mainApp.getAdapder().query("rooms, roomtypes", "roomtype,price", "roomNo=" + roomnumber + " and typeid = id");
        try {
            if (rs.next()) {
                this.roomType = rs.getString("roomtype");
                this.roomPrice = rs.getInt("price");
                this.roomPriceField.setText("" + this.roomPrice);
                this.roomTypeField.setText(this.roomType);
                this.totalPriceField.setText("" + this.roomPrice);
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
        if (ordersDisabled == false) {
            showRoomOrders();
        }
    }

    @FXML
    private void handleListCancelBtn() {
        if (OrderListView.getSelectionModel().getSelectedIndex() != -1) {
            CustomOrder tmp = (CustomOrder) OrderListView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Захиалга цуцлах");
            alert.setHeaderText("Та " + tmp.getid() + " дугаартай захиалга устгах гэж байна, үргэлжлүүлэх үү?");
            alert.setContentText("");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                mainApp.getAdapder().delete("orders", "id="+tmp.getid());
                showRoomOrders();
            } else {

            }
        }
    }

    @FXML
    private void handleListEditBtn() {
        int index = OrderListView.getSelectionModel().getSelectedIndex();
        CustomOrder tmpOrder = (CustomOrder) OrderListView.getItems().get(index);
        int selectedOrderNum = tmpOrder.getid();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BuonaNotte.class.getResource("view/Order.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Room order");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.dialogStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            OrderController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setDialogStage(dialogStage);
            controller.disableOrdersPanel();
            controller.editOrder(selectedOrderNum);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDateChange() {
        if (Period.between(LocalDate.now(), roomStartDateField.getValue()).getDays() < 0) {
            totalPriceField.setText("Сонгосон он сар алдаатай байна");
            saveable = false;
            return;
        }
        int day = Period.between(roomStartDateField.getValue(), roomEndDateField.getValue()).getDays();
        System.out.println(day);
        if (day > 0) {
            totalPriceField.setText("" + day * roomPrice);
            saveable = true;
        } else if (day == 0) {
            totalPriceField.setText("" + roomPrice);
            saveable = true;
        } else {
            totalPriceField.setText("Сонгосон он сар алдаатай байна");
            saveable = false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PaymentField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    PaymentField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (totalPriceField.getText().length() > 0 && PaymentField.getText().length() > 0) {
                    int pay = Integer.parseInt(PaymentField.getText());
                    int total = Integer.parseInt(totalPriceField.getText());
                    ReturnValueField.setText("" + (pay - total));
                }
            }
        });
        totalPriceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (totalPriceField.getText().length() > 0 && PaymentField.getText().length() > 0) {
                    int pay = Integer.parseInt(PaymentField.getText());
                    int total = Integer.parseInt(totalPriceField.getText());
                    ReturnValueField.setText("" + (pay - total));
                }
            }
        });
    }

    private void showRoomOrders() {
        OrderListView.getItems().clear();
        OrderListView.setCellFactory(new Callback<ListView<CustomOrder>, ListCell<CustomOrder>>() {

            @Override
            public ListCell<CustomOrder> call(ListView<CustomOrder> arg0) {
                return new ListCell<CustomOrder>() {
                    @Override
                    protected void updateItem(CustomOrder item, boolean bln) {
                        super.updateItem(item, bln);

                        if (item != null) {
                            VBox vBox = new VBox();
                            Text text = new Text();
                            text.setText("Order #" + item.getid());
                            text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                            vBox.getChildren().add(text);
                            Text text1 = new Text();
                            text1.setText(item.getDate());
                            text1.setFont(Font.font("Verdana", 11));
                            vBox.getChildren().add(text1);
                            setGraphic(vBox);
                        }
                    }

                };
            }

        });
        String roomnumber = ((Text) roomNumField.getSelectionModel().getSelectedItem()).getText();
        ResultSet rs = this.mainApp.getAdapder().query("orders", "id,checkin,checkout", "roomid=" + roomnumber + " and checkin >= '" + LocalDate.now() + "'");
        try {
            if (rs.next()) {
                OrderListView.getItems().add(new CustomOrder(rs.getString("checkin") + " - " + rs.getString("checkout"), rs.getInt("id")));

            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
    }

    private class CustomOrder {

        private String date;
        private int id;

        public String getDate() {
            return date;
        }

        public int getid() {
            return id;
        }

        public CustomOrder(String date, int id) {
            super();
            this.date = date;
            this.id = id;
        }
    }

    public void editOrder(int orderNo) {
        int roomno = 0;
        int guestid = 0;
        ResultSet rs = this.mainApp.getAdapder().query("orders,guests", "*", "orders.id=" + orderNo + " and guestid = guests.id");
        try {
            if (rs.next()) {
                FirstNameField.setText(rs.getString("firstname"));
                LastNameField.setText(rs.getString("lastname"));
                RegistryField.setText(rs.getString("registerid"));
                roomno = rs.getInt("roomid");
                guestid = rs.getInt("guestid");
                roomNumField.getItems().add(new Text(rs.getString("roomid")));
                roomNumField.getSelectionModel().select(0);
                roomNumField.setDisable(true);
                totalPriceField.setText(rs.getString("totalbill"));
                java.sql.Date tmpCheckin = rs.getDate("checkin");
                roomStartDateField.setValue(tmpCheckin.toLocalDate());
                java.sql.Date tmpCheckout = rs.getDate("checkout");
                roomEndDateField.setValue(tmpCheckout.toLocalDate());
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
            return;
        }
        rs = this.mainApp.getAdapder().query("rooms,roomtypes", "*", "roomNo=" + roomno + " and typeid = id");
        try {
            if (rs.next()) {
                roomTypeField.setText(rs.getString("roomtype"));
                roomPriceField.setText(rs.getString("price"));
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
            return;
        }
    }

    public void createOrder() {
        roomNumField.setDisable(false);
        ResultSet rs = this.mainApp.getAdapder().query("rooms", "*", "typeid=typeid");
        try {
            while (rs.next()) {
                roomNumField.getItems().add(new Text(rs.getString("roomNo")));
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
            return;
        }
        roomEndDateField.setValue(LocalDate.now());
        roomStartDateField.setValue(LocalDate.now());
    }

    @FXML
    private void handlePayment() {

    }
}
