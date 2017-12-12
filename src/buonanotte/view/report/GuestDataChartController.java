/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view.report;

import buonanotte.BuonaNotte;
import buonanotte.model.Guest;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author archer
 */
public class GuestDataChartController implements Initializable {
    @FXML
    TableView<Guest> guestTable;
    @FXML
    TableColumn<Guest, Number> idColumn;
    @FXML
    TableColumn<Guest, String> nameColumn;
    @FXML
    TableColumn<Guest, String> registerColumn;
    @FXML
    TableColumn<Guest, String> cardColumn;
    @FXML
    TableColumn<Guest, String> countryColumn;
    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    PieChart pieChart;
    @FXML
    CategoryAxis xAxis;
    @FXML
    NumberAxis yAxis;
    
    private BuonaNotte mainapp;
    private int selectedGuest;
    private String selectedGuestName;
    private ObservableList<String> monthNames = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.setLabel("Сар");
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
        yAxis.setLabel("Үйлчлүүлсэн хоног");
        lineChart.setTitle("Үйлчлүүлэгчийн буудалласан давтамжийн граф");
        pieChart.setTitle("Үйлчлүүлсэн өрөөний давтамж (төрлөөр)");
        guestTable.setOnMouseClicked((e) -> {
            if(e.getClickCount() == 2) {
                selectedGuest = guestTable.getSelectionModel().getSelectedItem().getGuestId();
                selectedGuestName = guestTable.getSelectionModel().getSelectedItem().getFullname();
                drawStackedBarChart(selectedGuest);
                drawPieChart(selectedGuest);
            }
        });
    }
    
    public void drawStackedBarChart(int id) {
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName(selectedGuestName);
        int i = 0;
        ResultSet rs = mainapp.getAdapder().query("orders", "count(*) as cnt", "guestid=" + id + " group by date_trunc('month', checkin)");
        try {
            while(rs.next()) {
                series.getData().add(new XYChart.Data(monthNames.get(i), rs.getInt("cnt")));
                i++;
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error at reading aggregate data: " + ex.getMessage());
        }
        lineChart.getData().clear();
        lineChart.getData().addAll(series);
    }
    
    public void drawPieChart(int id) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ResultSet rs = mainapp.getAdapder().query("(select typeid, roomid, count(orders.*) as cnt from orders, rooms where roomno = roomid and guestid = " + id + " group by roomid, typeid) as virtual, roomtypes", "roomtype, sum(cnt) as sum", "typeid = id group by typeid, roomtype");
        try {
            while(rs.next()) {
                pieChartData.add(new PieChart.Data(rs.getString("roomtype"), rs.getInt("sum")));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error at reading aggregate data: " + ex.getMessage());
        }
        pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " - ", data.pieValueProperty())));
        pieChart.setData(pieChartData);
    }
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }
    
    public void setMainApp(BuonaNotte app) {
        mainapp = app;
        guestTable.setItems(mainapp.getGuestData());
        idColumn.setCellValueFactory(data -> data.getValue().guestIdProperty());
        nameColumn.setCellValueFactory(data -> data.getValue().fullnameProperty());
        registerColumn.setCellValueFactory(data -> data.getValue().registerIdProperty());
        cardColumn.setCellValueFactory(data -> data.getValue().cardNumberProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().countryProperty());
    }
    
}
