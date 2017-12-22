package buonanotte.view.report;

import buonanotte.BuonaNotte;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.sf.jasperreports.view.*;
import net.sf.jasperreports.engine.*;

public class RoomsReportController implements Initializable {

    @FXML
    Button roomReportGenerateBtn;
    @FXML
    ProgressBar progressBar;
    @FXML
    Label stateLabel;
    private BuonaNotte mainapp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomReportGenerateBtn.setOnAction(e -> {
            new Thread( () -> {
                try {
                    String REPORT = "/Users/archer/NetBeansProjects/BuonaNotte/src/buonanotte/view/report/report1.jrxml";
                    JasperReport JASP_REP = JasperCompileManager.compileReport(REPORT);
                    Platform.runLater(() -> {progressBar.setProgress(0.3); stateLabel.setText("Өгөгдөл унших шат");});
                    JasperPrint JASP_PRINT = JasperFillManager.fillReport(JASP_REP, null, mainapp.getAdapder().getConnection());
                    Platform.runLater(() -> {
                        progressBar.setProgress(0.7);
                        stateLabel.setText("Дүрсэлж байна");
                    });
                    JasperViewer.viewReport(JASP_PRINT);
                    Platform.runLater(() -> {
                        progressBar.setProgress(0.9);
                        stateLabel.setText("Бэлтгэж дууслаа");
                    });
                } catch (JRException ex) {
                    System.out.println("Error while compiling report: " + ex.getMessage());
                }
                Platform.runLater(() -> progressBar.setProgress(1));
            }).start();
        });
    }

    public void setMainApp(BuonaNotte app) {
        mainapp = app;
    }

    public BuonaNotte getMainApp() {
        return mainapp;
    }

}
