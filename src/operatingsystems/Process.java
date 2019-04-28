/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystems;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Random;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author amjad
 */
public class Process extends RecursiveTreeObject<Process> {
    static int ProcessNumber;
    private int pid;
    private int remainingTimeCPU; 
    private int remainingTimeIO; 
    private int priority;
    private int arrivalTime;
    private int cpuBurst;
    private int startTime; 
    private int turnAround;
    private int finishTime; 
    private int waitingTime;
    private int reqIO;
    private int ioBurst;
    private int totalInstructions;
    private int cpuInstrct;
    private int ioInstrct;
    private SimpleIntegerProperty tablePid,tableArrivalTime,tableStartTime,tableEndTime,tableTurnAround,
            tableReqIo,tableIoBurst,tableCpuBurst,tableWait;

    
    
    public Process(int quantum) {
        	Random rand = new Random();
                this.totalInstructions = (int) Math.abs(rand.nextGaussian()*1000); //gives an average of 750~
                if(this.ProcessNumber % 3 == 0)//Every third process generated has IO instructions!
                {
                    double ioPercentage = (double)(rand.nextInt(6)+1)/10;
                    this.ioInstrct = (int) (ioPercentage*this.totalInstructions);
                    this.cpuInstrct = this.totalInstructions - this.ioInstrct;
                    this.cpuBurst = (int) Math.ceil((double)this.cpuInstrct/(double)quantum);
                    this.ioBurst = (int) Math.ceil((double)this.ioInstrct/(double)quantum);
                    this.reqIO = (int) Math.abs(rand.nextInt(this.cpuBurst));
                }
                else
                {
                    this.cpuInstrct = this.totalInstructions;
                    this.ioInstrct = 0;
                    this.ioBurst = 0;
                    this.cpuBurst = (int) Math.ceil((double)this.totalInstructions/(double)quantum);
                    this.reqIO = -1;
                }
		this.priority = rand.nextInt(5) + 1;
		this.pid = ProcessNumber;
		ProcessNumber++;
		this.arrivalTime = rand.nextInt(10) + 1;
                this.startTime = -1;
                this.finishTime = -1;
                this.turnAround = -1;
                this.remainingTimeCPU = this.cpuBurst;
                this.remainingTimeIO = this.ioBurst;
                
                //TableView Items
                this.tablePid = new SimpleIntegerProperty(this.pid);
                this.tableArrivalTime = new SimpleIntegerProperty(this.arrivalTime);
                this.tableStartTime = new SimpleIntegerProperty(this.startTime);
                this.tableEndTime = new SimpleIntegerProperty(this.finishTime);
                this.tableReqIo = new SimpleIntegerProperty(this.reqIO);
                this.tableIoBurst = new SimpleIntegerProperty(this.ioBurst);
                this.tableCpuBurst = new SimpleIntegerProperty(this.cpuBurst);
                this.tableTurnAround = new SimpleIntegerProperty(this.turnAround);
                this.tableWait = new SimpleIntegerProperty(this.waitingTime);
                
    }
    
    public void refreshTableItems(){
        this.tablePid = new SimpleIntegerProperty(this.pid);
        this.tableArrivalTime = new SimpleIntegerProperty(this.arrivalTime);
        this.tableStartTime = new SimpleIntegerProperty(this.startTime);
        this.tableEndTime = new SimpleIntegerProperty(this.finishTime);
        this.tableReqIo = new SimpleIntegerProperty(this.reqIO);
        this.tableTurnAround = new SimpleIntegerProperty(this.turnAround);
        this.tableCpuBurst = new SimpleIntegerProperty(this.cpuBurst);
        this.tableTurnAround = new SimpleIntegerProperty(this.turnAround);
        this.tableWait = new SimpleIntegerProperty(this.waitingTime);
    }
    
    public void decCPU(){
        this.remainingTimeCPU--;
    }
    
    public void decIO(){
        this.remainingTimeIO--;
    }

    @Override
    public String toString() {
        return "Process{" + "pid=" + pid + ", cpuBurst=" + cpuBurst + ", startTime=" + startTime + ", reqIO=" + reqIO + ", ioBurst=" + ioBurst + ", totalInstructions=" + totalInstructions + ", start=" + startTime + ", end=" + finishTime + '}';
    }
    
    
    public static int getProcessNumber() {
        return ProcessNumber;
    }

    public static void setProcessNumber(int ProcessNumber) {
        Process.ProcessNumber = ProcessNumber;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getRemainingTimeCPU() {
        return remainingTimeCPU;
    }

    public void setRemainingTimeCPU(int remainingTimeCPU) {
        this.remainingTimeCPU = remainingTimeCPU;
    }

    public int getRemainingTimeIO() {
        return remainingTimeIO;
    }

    public void setRemainingTimeIO(int remainingTimeIO) {
        this.remainingTimeIO = remainingTimeIO;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(int cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getTurnAround() {
        return turnAround;
    }

    public void setTurnAround(int turnAround) {
        this.turnAround = turnAround;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getReqIO() {
        return reqIO;
    }

    public void setReqIO(int reqIO) {
        this.reqIO = reqIO;
    }

    public int getIoBurst() {
        return ioBurst;
    }

    public void setIoBurst(int ioBurst) {
        this.ioBurst = ioBurst;
    }

    public int getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(int totalInstructions) {
        this.totalInstructions = totalInstructions;
    }

    public int getCpuInstrct() {
        return cpuInstrct;
    }

    public void setCpuInstrct(int cpuInstrct) {
        this.cpuInstrct = cpuInstrct;
    }

    public int getIoInstrct() {
        return ioInstrct;
    }

    public void setIoInstrct(int ioInstrct) {
        this.ioInstrct = ioInstrct;
    }
    
    
    public int getTablePid() {
        return tablePid.get();
    }

    public void setTablePid(SimpleIntegerProperty tablePid) {
        this.tablePid = tablePid;
    }

    public int getTableArrivalTime() {
        return tableArrivalTime.get();
    }

    public void setTableArrivalTime(SimpleIntegerProperty tableArrivalTime) {
        this.tableArrivalTime = tableArrivalTime;
    }

    public int getTableStartTime() {
        return tableStartTime.get();
    }

    public void setTableStartTime(int time) {
        this.tableStartTime = tableStartTime;
    }
    
    
    public int getTableEndTime() {
        return tableEndTime.get();
    }

    public void setTableEndTime(SimpleIntegerProperty tableEndTime) {
        this.tableEndTime = tableEndTime;
    }

    public int getTableTurnAround() {
        return tableTurnAround.get();
    }

    public void setTableTurnAround(SimpleIntegerProperty tableTurnAround) {
        this.tableTurnAround = tableTurnAround;
    }

    public int getTableReqIo() {
        return tableReqIo.get();
    }

    public void setTableReqIo(SimpleIntegerProperty tableReqIo) {
        this.tableReqIo = tableReqIo;
    }

    public int getTableIoBurst() {
        return tableIoBurst.get();
    }

    public void setTableIoBurst(SimpleIntegerProperty tableIoBurst) {
        this.tableIoBurst = tableIoBurst;
    }

    public int getTableCpuBurst() {
        return tableCpuBurst.get();
    }

    public void setTableCpuBurst(SimpleIntegerProperty tableCpuBurst) {
        this.tableCpuBurst = tableCpuBurst;
    }

    public int getTableWait() {
        return tableWait.get();
    }

    public void setTableWait(SimpleIntegerProperty tableWait) {
        this.tableWait = tableWait;
    }

    
    
    
}
