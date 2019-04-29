/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystems;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author amjad
 */
public class ProcListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    public TableView<Process> tableView;
    @FXML
    private JFXButton closeButton;
    @FXML
    private TableColumn<Process,Integer> PID;
    @FXML
    private TableColumn<Process,Integer> ARRIVAL;
    @FXML
    private TableColumn<Process,Integer> PRIORITY;
    @FXML
    private TableColumn<Process,Integer> START;
    @FXML
    private TableColumn<Process,Integer> END;
    @FXML
    private TableColumn<Process,Integer> TA;
    @FXML
    private TableColumn<Process,Integer> IO;
    @FXML
    private TableColumn<Process,Integer> IOBURST;
    @FXML
    private TableColumn<Process,Integer> CPUBURST;
    @FXML
    private TableColumn<Process,Integer> WAIT;
    @FXML
    private AnchorPane dragPane;
    private double xOffset,yOffset;
    
    public ObservableList<Process> Processes = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        PID.prefWidthProperty().bind(tableView.widthProperty().divide(5));
//        ARRIVAL.prefWidthProperty().bind(tableView.widthProperty().divide(5));
//        END.prefWidthProperty().bind(tableView.widthProperty().divide(5));
//        TA.prefWidthProperty().bind(tableView.widthProperty().divide(5));
//        IO.prefWidthProperty().bind(tableView.widthProperty().divide(5));
//        
        
        dragPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = dragPane.getScene().getWindow().getX() - event.getScreenX();
                yOffset = dragPane.getScene().getWindow().getY() - event.getScreenY();
            }
        });
        dragPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragPane.getScene().getWindow().setX(event.getScreenX() + xOffset);
                dragPane.getScene().getWindow().setY(event.getScreenY() + yOffset);
            }
        });
//        
        PID.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tablePid"));
        ARRIVAL.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableArrivalTime"));
        PRIORITY.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tablePriority"));
        START.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableStartTime"));
        END.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableEndTime"));
        TA.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableTurnAround"));
        IO.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableReqIo"));
        CPUBURST.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableCpuBurst"));
        IOBURST.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableIoBurst"));
        WAIT.setCellValueFactory(new PropertyValueFactory<Process,Integer>("tableWait"));
       
       for(Process i : Scheduler.Processes)
       {
           Processes.add(i);
       }
       tableView.setItems(Processes);
    }
    
    @FXML
    public void refreshTable(){
        Processes.clear();
        for(Process i : Scheduler.Processes)
        {
            Processes.add(i);
            i.refreshTableItems();
        }
//        tableView.setItems(Processes);
          tableView.refresh();
    }
    
    @FXML
    private void ExitApp(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
}
    
