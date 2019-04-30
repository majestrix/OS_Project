/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystems;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author amjad
 */
public class MainController implements Initializable {

    @FXML
    private TextField procNum,MP;
    @FXML
    private TextField timeQuantum;
    @FXML
    private Label generateLabel;
    @FXML
    private JFXComboBox selectAlgo;
    @FXML
    private JFXButton simButton;
    @FXML
    private Label fFCFS,wFCFS,fRR,wRR,fPP,wPP,fPN,wPN,fSJF,wSJF,fSRTF,wSRTF,
                    utFCFS,utRR,utPP,utPN,utSJF,utSRTF;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> options = FXCollections.observableArrayList("FCFS","RR",
                                         "Priority(N)","Priority(P)","SJF","SRTF");
        selectAlgo.getItems().addAll(options);
    }    

    @FXML
    private void Generate(ActionEvent event) {
        int procNumINT;
        int timeQuantumINT;
        Scheduler.clear();
        try
        {
            if( procNum.getText() != null && timeQuantum.getText() != null)
            {
                procNumINT = Integer.parseInt(procNum.getText());
                timeQuantumINT = Integer.parseInt(timeQuantum.getText());
                generateLabel.setText("OOP" + procNumINT);
                for (int i = 0; i < procNumINT; i++) {
                    Process process = new Process(timeQuantumINT);
                    System.out.println(process.toString());
                    Scheduler.Processes.add(process);
                }
                generateLabel.setText("Generated: " + procNumINT);
            }
        else
            generateLabel.setText("Please enter provide input!");
        }
        catch(NumberFormatException e)
        {
            generateLabel.setText("Please enter a valid number/s");
        }
       

    }


    @FXML
    public void ShowProcs(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("procList.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(tableViewScene);
        stage.show();
    }
    @FXML
    private void ExitApp(ActionEvent event) throws IOException {
        System.exit(0);
    }
    
    @FXML
    private void simulateData() throws IOException{
        String option;
        int intMP;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("procList.fxml"));
        loader.load();
        ProcListController cont = loader.getController();
        simButton.setText("Simulate");
        
        try{
          option = selectAlgo.getValue().toString();
          intMP = Integer.parseInt(MP.getText());
        }catch(NullPointerException e)
        {
            option = "";
            intMP = 10;
        }
        Scheduler.setMP(intMP);
        //Find Averages
        Scheduler.FCFS();
        this.wFCFS.setText(Scheduler.calcAverageWait() + "");
        this.fFCFS.setText(Scheduler.calcAverageTurnAround() + "");
        this.utFCFS.setText(Scheduler.getUt()+"%");
        Scheduler.RR();
        this.wRR.setText(Scheduler.calcAverageWait() + "");
        this.fRR.setText(Scheduler.calcAverageTurnAround() + "");
        this.utRR.setText(Scheduler.getUt()+"%");
        Scheduler.PN();
        this.wPN.setText(Scheduler.calcAverageWait() + "");
        this.fPN.setText(Scheduler.calcAverageTurnAround() + "");
        this.utPN.setText(Scheduler.getUt()+"%");
        Scheduler.PP();
        this.wPP.setText(Scheduler.calcAverageWait() + "");
        this.fPP.setText(Scheduler.calcAverageTurnAround() + "");
        this.utPP.setText(Scheduler.getUt() + "%");
        Scheduler.SJF();
        this.wSJF.setText(Scheduler.calcAverageWait() + "");
        this.fSJF.setText(Scheduler.calcAverageTurnAround() + "");
        this.utSJF.setText(Scheduler.getUt()+"%");
        Scheduler.SRTF();
        this.wSRTF.setText(Scheduler.calcAverageWait() + "");
        this.fSRTF.setText(Scheduler.calcAverageTurnAround() + "");
        this.utSRTF.setText(Scheduler.getUt()+"%");

        
        switch(option)
        {
            case "FCFS":
                System.out.println("Initiating First come first server!");
                Scheduler.FCFS();
                break;
            case "RR":
                System.out.println("Initiating Round Robin!");
                Scheduler.RR();
                break;
            case "Priority(N)":
                System.out.println("Initiating Non-preemptive Priority!!");
                Scheduler.PN();
                break;
            case "Priority(P)":
                System.out.println("Initiating Pre-emptive Priority!");
                Scheduler.PP();
                break;
            case "SJF":
                System.out.println("Initiating ShortestRemainingTimeFirst!");
                Scheduler.SJF();
                break;
            case "SRTF":
                System.out.println("Initiating ShortestRemainingTimeFirst!");
                Scheduler.SRTF();
                break;
            default:
                simButton.setText("Select first!");
                break;
        }
        cont.refreshTable();
    }
    
    
}
