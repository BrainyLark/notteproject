/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buonanotte.view.report;

import buonanotte.BuonaNotte;
import buonanotte.model.Room;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author archer
 */
public class IncomeUsageChartController implements Initializable {
    
    @FXML
    TextField startMonth;
    @FXML
    TextField endMonth;
    @FXML
    Button pieChartButton;
    @FXML
    Label incomeLabel;
    @FXML
    Label orderLabel;
    @FXML
    TableView<Room> roomTable;
    @FXML
    TableColumn<Room, Number> idColumn;
    @FXML
    TableColumn<Room, String> typeColumn;
    @FXML
    TableColumn<Room, Number> priceColumn;
    @FXML
    Button areaChartButton;
    @FXML
    PieChart pieChart;
    @FXML
    AreaChart<String, Integer> areaChart;
    @FXML
    CategoryAxis xAxis;
    
    private BuonaNotte mainapp;
    private int selectedRoom;
    private ObservableList<String> monthNames = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
        List<Double> yAxisValues = new ArrayList();
        pieChartButton.setOnAction((e) -> {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            String smonth = startMonth.getText();
            String emonth = endMonth.getText();
            double totalincome = 0;
            int totalorder = 0;
            double bill;
            ResultSet rs = mainapp.getAdapder().query("orders", "roomid as id, sum(totalbill) as bill, count(*) as cnt", "checkin between '2017-" + smonth + "' and '2017-" + emonth + "' group by roomid");
            try {
                while(rs.next()) {
                    bill = rs.getDouble("bill");
                    pieChartData.add(new PieChart.Data("Room " + rs.getInt("id"), + bill));
                    totalincome += bill;
                    totalorder += rs.getInt("cnt");
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Error occured while reading orders: " + ex.getMessage());
            }
            pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " - ", data.pieValueProperty(), "₮")));
            pieChart.setData(pieChartData);
            incomeLabel.setText(String.format("%.2f", totalincome) + "₮");
            orderLabel.setText(String.valueOf(totalorder));
         });
        
        areaChartButton.setOnAction(e -> {
            selectedRoom = roomTable.getSelectionModel().getSelectedItem().getNumber();
            yAxisValues.clear();
            ResultSet rs = mainapp.getAdapder().query("orders", "sum(totalbill) as bill", "roomid=" + selectedRoom + " group by date_trunc('month', checkin)");
            try {
                while(rs.next()) {
                    yAxisValues.add(rs.getDouble("bill"));
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Error occured while reading total bill of room: " + ex.getMessage());
            }
            XYChart.Series<String, Integer> series = new XYChart.Series();
            for(int i = 0; i < yAxisValues.size(); i++) {
                series.getData().add(new XYChart.Data(monthNames.get(i), yAxisValues.get(i)));
                series.setName(String.valueOf(selectedRoom));
            }
            if(areaChart.getData().size() == 3) {
                areaChart.getData().remove(0);
            }
            areaChart.getData().addAll(series);
        });
    }    
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }
    
    public void setMainApp(BuonaNotte app) {
        mainapp = app;
        roomTable.setItems(mainapp.getRoomData());
        idColumn.setCellValueFactory(data -> data.getValue().numberProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().getType().typeProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().getType().priceProperty());
    }
}
