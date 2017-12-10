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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author archer
 */
public class RoomUsageChartController implements Initializable {

    @FXML
    TableView<Room> roomTable;
    @FXML
    TableColumn<Room, Number> roomColumn;
    @FXML
    TableColumn<Room, String> typeColumn;
    @FXML
    Button button;
    @FXML
    BarChart<String, Integer> barChart;
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
        List<Integer> yAxisValues = new ArrayList();
        button.setOnAction((e) -> {
            selectedRoom = roomTable.getSelectionModel().getSelectedItem().getNumber();
            barChart.setTitle("Өрөө ашигласан давтамж (сараар)");
            yAxisValues.clear();
            ResultSet rs = mainapp.getAdapder().query("orders", "count(*) as cnt", "roomid=" + selectedRoom + " group by date_trunc('month', checkin)");
            try {
                while(rs.next()) {
                    yAxisValues.add(rs.getInt("cnt"));
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Error occured while reading orders: " + ex.getMessage());
            }
            XYChart.Series<String, Integer> series = new XYChart.Series();
            for(int i = 0; i < yAxisValues.size(); i++) {
                series.getData().add(new XYChart.Data(monthNames.get(i), yAxisValues.get(i)));
                series.setName(String.valueOf(selectedRoom));
            }
            if(barChart.getData().size() == 4)
                barChart.getData().remove(0);
            barChart.getData().add(series);
        });
    }
    
    public BuonaNotte getMainApp() {
        return mainapp;
    }
    
    public void setMainApp(BuonaNotte app) {
        mainapp = app;
        roomTable.setItems(getMainApp().getRoomData());
        roomColumn.setCellValueFactory(celldata -> celldata.getValue().numberProperty());
        typeColumn.setCellValueFactory(celldata -> celldata.getValue().getType().typeProperty());
    }
    
}
