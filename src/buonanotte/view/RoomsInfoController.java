/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view;

import buonanotte.BuonaNotte;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author bilguun
 */
public class RoomsInfoController implements Initializable {
    
    private BuonaNotte mainapp;

    @FXML
    private TilePane MainTilePane;
    @FXML
    private ListView AllOrdersList;
    
    private Stage dialogStage;
    @FXML
    private ComboBox roomTypeCombo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    public void test(String roomtype){
        MainTilePane.getChildren().clear();
        ResultSet rs;
        if(roomtype.length() < 2){
            rs= this.mainapp.getAdapder().query("rooms","roomno", "true");
        }else{
            rs= this.mainapp.getAdapder().query("rooms, roomtypes", "roomno","typeid = roomtypes.id and roomtype='"+roomtype+"'");
        }
        try {
            while(rs.next()) {
                MainTilePane.getChildren().add(createElement(rs.getInt("roomno")));
            }
        } 
        catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
    }
    public void setMainApp(BuonaNotte mainApp){
        this.mainapp = mainApp;
        test(" ");
        setAllOrders();
        setRoomType();
        roomTypeCombo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                test(newValue);
            }
    });
    }
    private StackPane createElement(int number) {
        StackPane stack = new StackPane();
        Rectangle rectangle = new Rectangle(100, 100);
        ResultSet rs = this.mainapp.getAdapder().query("orders","id","roomid ="+ number+" and checkin <='"+LocalDate.now()+"' and checkout >= '"+LocalDate.now()+"'");
        try {
            if(rs.next()) {
                rectangle.setFill(Color.rgb(50,72,81));
                int id = rs.getInt("id");
                Text text = new Text(""+number);
                text.setFill(Color.WHITE);
                text.setFont(Font.font("Verdana",14));
                stack.getChildren().addAll(rectangle,text);
                stack.setOnMouseClicked((event) -> {
                    mainapp.showExitDialog(id);
                });
            }
            else{
                rectangle.setFill(Color.rgb(134,172,65));
                Text text = new Text(""+number);
                text.setFill(Color.WHITE);
                text.setFont(Font.font("Verdana",14));
                stack.getChildren().addAll(rectangle, text);
                stack.setOnMouseClicked((event) -> {
                    mainapp.showOrderDialog(number);
                });
            }
            rectangle.setStrokeType(StrokeType.INSIDE);
            rectangle.setStroke(Color.GRAY);
            rectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            rectangle.setArcWidth(10);
            rectangle.setArcHeight(10);
            rectangle.setStyle("-fx-border-radius:10; -fx-border-color:GRAY; -fx-border-width:1; -fx-background-radius:10;");
        } 
        catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
        stack.setOnMouseEntered((event) -> {
                    stack.setOpacity(0.8);
                });
        stack.setOnMouseExited((event) -> {
                    stack.setOpacity(1);
                });
        return stack;
    }

    public void setAllOrders() {
        AllOrdersList.getItems().clear();
        AllOrdersList.setCellFactory(new Callback<ListView<CustomOrder>, ListCell<CustomOrder>>() {
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
        ResultSet rs = this.mainapp.getAdapder().query("orders","id,checkin,checkout","checkin >= '"+LocalDate.now()+"'");
        try {
            while(rs.next()) {
                AllOrdersList.getItems().add(new CustomOrder(rs.getString("checkin")+" - "+rs.getString("checkout"),rs.getInt("id")));
            }
        }
        catch (SQLException ex) {
            System.out.println("The error has been occured while querying user data: " + ex.getMessage());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void setRoomType() {
        ResultSet rs = this.mainapp.getAdapder().query("roomtypes", "*", "id = id");
        roomTypeCombo.getItems().clear();
        roomTypeCombo.getItems().add(" ");
        try {
            while(rs.next()) {
                roomTypeCombo.getItems().add(rs.getString("roomtype"));
            }
        }
        catch (SQLException ex) {
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
    @FXML
    public void handleCreateOrder(){
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
            controller.setMainApp(mainapp);
            controller.setDialogStage(dialogStage);
            controller.createOrder();
            dialogStage.setOnHiding( event -> {setAllOrders(); test(" ");} );
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleEditBtn(){
        int index = AllOrdersList.getSelectionModel().getSelectedIndex();
        CustomOrder tmpOrder = (CustomOrder) AllOrdersList.getItems().get(index);
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
            controller.setMainApp(mainapp);
            controller.setDialogStage(dialogStage);
            controller.disableOrdersPanel();
            controller.editOrder(selectedOrderNum);
            dialogStage.setOnHiding( event -> {setAllOrders(); test(" ");} );
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleDeleteBtn(){
        if(AllOrdersList.getSelectionModel().getSelectedIndex() != -1){
            CustomOrder tmp = (CustomOrder) AllOrdersList.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Захиалга цуцлах");
            alert.setHeaderText("Та "+tmp.getid()+" дугаартай захиалга устгах гэж байна, үргэлжлүүлэх үү?");
            alert.setContentText("");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                mainapp.getAdapder().delete("orders", "id="+tmp.getid());
                test(" ");
                setAllOrders();
            } else {
                
            }
        }
    }
    @FXML
    public void handleExit(){
        dialogStage.close();
    }
}