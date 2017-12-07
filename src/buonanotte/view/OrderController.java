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
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class OrderController implements Initializable {
    
    @FXML
    private Label roomNumField;
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
    
    
    
    private BuonaNotte mainApp;
    private int roomNum = -1;
    private Stage dialogStage;
    private String roomType;
    private int roomPrice; 
    private boolean saveable = true;
    /**
    * The constructor
    **/
    public OrderController(){
        
    }
    
    public void setMainApp(BuonaNotte mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setRoom(int roomNumber) {
        this.roomNum = roomNumber;
        showRoomOrders();
        ResultSet rs = this.mainApp.getAdapder().query("rooms, roomtypes","roomtype,price","roomNo="+roomNumber+" and typeid = id");
        try {
            if(rs.next()) {
                this.roomType = rs.getString("roomtype");
                this.roomPrice = rs.getInt("price");
                this.roomNumField.setText(""+this.roomNum);
                this.roomPriceField.setText(""+this.roomPrice);
                this.roomTypeField.setText(this.roomType);
                this.roomStartDateField.setValue(LocalDate.now());
                this.roomEndDateField.setValue(LocalDate.now());
                this.totalPriceField.setText(""+this.roomPrice);
            }
        } catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
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
        if(registry.length() != 10) {
              ErrorField.setText("Регистерийн дугаар буруу");
              return;
        }
        int sar,udur;
        if(m.find()){
            sar = Integer.parseInt(m.group(2));
            udur = Integer.parseInt(m.group(2));
        }else {
            ErrorField.setText("Регистерийн дугаар буруу");
            return;
        }
        if(sar>12 || udur > 31) {
            ErrorField.setText("Регистерийн дугаар буруу");
            return;
        }
        if(FirstNameField.getText().length() > 1 && LastNameField.getText().length() > 1 && saveable == true){
            boolean a = false;
            a = this.mainApp.getAdapder().insert("orders","'"+roomStartDateField.getValue()+"','"+roomEndDateField.getValue()+"',"+ totalPriceField.getText()+","+roomNum+",1");
            if(a){
                ErrorField.setText("Хадгалагдлаа");
            }else{
                ErrorField.setText("Алдаа гарлаа");
                return;
            }
        }else{
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
    private void handleListCancelBtn(){
        System.out.println(""+OrderListView.getSelectionModel().getSelectedIndex());
    }
    @FXML
    private void handleListEditBtn(){
        System.out.println(""+OrderListView.getSelectionModel().getSelectedIndex());
    }
    @FXML
    private void handleDateChange(){
        if(Period.between(LocalDate.now(),roomStartDateField.getValue()).getDays()<0){
            totalPriceField.setText("Сонгосон он сар алдаатай байна");
            System.out.println(Period.between(LocalDate.now(),roomStartDateField.getValue()).getDays());
            saveable = false;
            return;
        }
        int day = Period.between(roomStartDateField.getValue(),roomEndDateField.getValue()).getDays();
        System.out.println(day);
        if(day > 0) {
            totalPriceField.setText(""+day*roomPrice);
            saveable = true;
        }
        else if(day == 0) {
            totalPriceField.setText(""+roomPrice);
            saveable = true;
        }
        else{
            totalPriceField.setText("Сонгосон он сар алдаатай байна");
            saveable = false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    private void showRoomOrders() {
        ObservableList<CustomOrder> data = FXCollections.observableArrayList();
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
                            text.setText("Order #"+item.getid());
                            text.setFont(Font.font("Verdana",FontWeight.BOLD,12));
                            vBox.getChildren().add(text);
                            Text text1 = new Text();
                            text1.setText(item.getDate());
                            text1.setFont(Font.font("Verdana",11));
                            vBox.getChildren().add(text1);
                            setGraphic(vBox);
                        }
                    }

                };
            }

        });
        ResultSet rs = this.mainApp.getAdapder().query("orders","id,checkin,checkout","roomid="+roomNum+" and checkin >= '"+LocalDate.now()+"'");
        try {
            if(rs.next()) {
                OrderListView.getItems().add(new CustomOrder(rs.getString("checkin")+" - "+rs.getString("checkout"),rs.getInt("id")));
                
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
}
