/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystems;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author amjad
 */
public class Scheduler {

    public static ArrayList<Process> Processes = new ArrayList<Process>();
    public static ArrayList<Process> readyQueue;
    public static ArrayList<Process>  waitQueue;
    public static int timeQuantum;
    private static int MP = 10;
    
    public void Scheduler(){
    }
    
    public static void clear(){
        Scheduler.Processes.clear();
        Process.ProcessNumber = 0;
    }
    
    public static boolean processesRunning() { // function to check if all process has finished or not
            for (Process p : Processes)
                    if (p.getFinishTime() == -1) // if any process has finish time = -1, then it hasn't finished yet
                            return true;
            return false;
    }
    
//    public boolean processFinished(){
//        if( this.currentlyRunning != null) {
//            return this.currentlyRunning.getRemainingTime() == 0;
//         }
//        return false;
//    }

//    public static void FCFS() {
//		int time = 0; // current time
//		readyQueue = new ArrayList<Process>();
//                waitQueue = new ArrayList<Process>();
//                Process currentlyRunning = null;
//		while (processesRunning()) // while there are still unfinished processes
//                {
////                        for(Process p: Processes)
////                            System.out.print("P"+p.getPid()+"-"+p.getFinishTime()+"|");
////                        System.out.println();
//
//			for (Process p : Processes)
//                            if (p.getArrivalTime() == time)
//                                    readyQueue.add(p);
//                        
//                        if (currentlyRunning == null && !readyQueue.isEmpty()) //if no process has started
//                        {
//                            currentlyRunning = readyQueue.remove(0); //pop the process from readyQ
//                            if (currentlyRunning != null) //check if we actually got a process and start it
//                                    currentlyRunning.setTableStartTime(time);
//			}
//                        
//                        if(currentlyRunning != null && currentlyRunning.getRemainingTimeCPU() == currentlyRunning.getReqIO() && currentlyRunning.getReqIO() >= 0 && currentlyRunning.getRemainingTimeIO() > 0)
//                        {
//                            waitQueue.add(currentlyRunning);    //Check if process requesting IO
//                            currentlyRunning = null;            //Before going for another CPU burst
//
//                        }
//                        
//                        if(!waitQueue.isEmpty())
//                        {
//                            Iterator<Process> iter = waitQueue.iterator();
//                            
//                            while(iter.hasNext() && currentlyRunning != null)
//                            {
//                                Process p = iter.next();
//                                p.decIO();
//                                if(p.getRemainingTimeIO() <= 0)
//                                {
//                                    readyQueue.add(p);
//                                    iter.remove();
//                                }
//                            }
//                        }
//			
//
//			if (currentlyRunning != null) //Work calculations here!
//                        {
//                            currentlyRunning.decCPU(); //Decrement CPU Remanining time
//                            if(currentlyRunning.getRemainingTimeCPU() == 0)//Process Finished!
//                            {
//                                System.out.print("| P("+currentlyRunning.getPid()+") | ");
//                                currentlyRunning.setFinishTime(time);
//                                currentlyRunning.refreshTableItems();
//                                int startTime = Integer.parseInt(currentlyRunning.getTableStartTime().split(",")[1]);
//                                currentlyRunning.setTurnAround(currentlyRunning.getFinishTime() - startTime);
//
//                                currentlyRunning.setWaitingTime(currentlyRunning.getTurnAround() - currentlyRunning.getCpuBurst()); 
//                                currentlyRunning = null;
//                                
//                            }
//                            //Reschedule if process finished/sent to waitQ
//                            if (currentlyRunning == null && !readyQueue.isEmpty())
//                            {
//                              currentlyRunning = readyQueue.remove(0);
//                                if (currentlyRunning != null)
//                                    currentlyRunning.setStartTime(time);  
//                            }
//                                    
//			}
//
//			//addToGanttChart(ganttChart, currentlyRunning); // add currently running process at the current moment t													// Gantt Chart
//			time++; // increment time
//		}
//		//return ganttChart;
//	}
    public static void FCFS(){
        int time = 0;
        readyQueue = new ArrayList<Process>();
        waitQueue = new ArrayList<Process>();
        Process curRunning = null;
        
        while(processesRunning()){
        
            for (Process p : Processes)
                if (p.getArrivalTime() == time && readyQueue.size() < MP)
                {
                    readyQueue.add(p);
                } //Keep checking if we can add
            
            if(!waitQueue.isEmpty())
            {
                Iterator<Process> iter = waitQueue.iterator();

                while(iter.hasNext())
                {
                    Process p = iter.next();
                    p.decIO();
                    if(p.getRemainingTimeIO() <= 0)
                    {
                        readyQueue.add(p);
                        iter.remove();
                    }
                }
            }
            
            if(curRunning == null) //if not working, start!
            {
                if(readyQueue.size() > 0)
                {
                    curRunning = readyQueue.remove(0);
                    if(curRunning.getStartTime() == -1)
                        curRunning.setStartTime(time);
                }
            }
            
            
            if(curRunning != null)
            {//Work calculations!
                //First Check if process asks for IO!
                if(curRunning.getReqIO() != -1 && curRunning.getRemainingTimeIO() > 0 &&
                        curRunning.getReqIO()+curRunning.getRemainingTimeCPU() == curRunning.getCpuBurst())
                {
                    waitQueue.add(curRunning);
                    curRunning = null;
                }
                else//If not, do the CPU math!
                {
                    curRunning.decCPU();
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        curRunning = null;
                    }
                }
            }
            time++;
        }
    }
    
    public static void RR(){
            int time = 0;
        readyQueue = new ArrayList<Process>();
        waitQueue = new ArrayList<Process>();
        Process curRunning = null;
        
        while(processesRunning()){
        
            for (Process p : Processes)
                if (p.getArrivalTime() == time && readyQueue.size() < MP)
                {
                    readyQueue.add(p);
                } //Keep checking if we can add
            
            if(!waitQueue.isEmpty())
            {
                Iterator<Process> iter = waitQueue.iterator();

                while(iter.hasNext())
                {
                    Process p = iter.next();
                    p.decIO();
                    if(p.getRemainingTimeIO() <= 0)
                    {
                        readyQueue.add(p);
                        iter.remove();
                    }
                }
            }
            
            if(curRunning == null) //if not working, start!
            {
                if(readyQueue.size() > 0)
                {
                    curRunning = readyQueue.remove(0);
                    if(curRunning.getStartTime() == -1)
                        curRunning.setStartTime(time);
                }
            }
            
            if(curRunning != null)
            {//Work calculations!
                //First Check if process asks for IO!
                if(curRunning.getReqIO() != -1 && curRunning.getRemainingTimeIO() > 0 &&
                        curRunning.getReqIO()+curRunning.getRemainingTimeCPU() == curRunning.getCpuBurst())
                {
                    waitQueue.add(curRunning);
                    curRunning = null;
                }
                else//If not, do the CPU math!
                {
                    curRunning.decCPU();
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        curRunning = null;
                    }
                    else
                    {
                        readyQueue.add(curRunning);
                        curRunning = null;
                    }
                }
            }
            time++;
        }
    }
}
