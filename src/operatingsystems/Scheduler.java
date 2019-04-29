/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operatingsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javafx.util.Pair;

/**
 *
 * @author amjad
 */
public class Scheduler {

    public static ArrayList<Process> Processes = new ArrayList<Process>();
    public static ArrayList<Process> readyQueue;
    public static ArrayList<Process>  waitQueue;
    public static ArrayList<Process>  newQueue;
    public static int timeQuantum;
    private static int MP = 10;
    
    public void Scheduler(){
    }
    
    public static void clear(){
        Scheduler.Processes.clear();
        Process.ProcessNumber = 0;
    }
    
    public static void resetProcs(){
        for(Process p : Processes)
            p.resetProcess();
    }
    
    public static boolean processesRunning() { // function to check if all process has finished or not
            for (Process p : Processes)
                    if (p.getFinishTime() == -1) // if any process has finish time = -1, then it hasn't finished yet
                            return true;
            return false;
    }
    
    public static int calcAverageWait(){
        int sum = 0;
        int avg;
        for(Process p: Processes)
        {
            sum += p.getWaitingTime();
        }
        return sum/Processes.size();
    }
    
    public static void insertSorted(Process p,ArrayList<Process> arr){
        Comparator<Process> comp = Comparator.comparing(process -> process.getPriority());
        comp = comp.thenComparing(Comparator.comparing(process -> process.getArrivalTime()));
        int pos = Collections.binarySearch(arr,p,comp);
        if (pos < 0) {
            arr.add(-pos-1, p);
        }
        else
            arr.add(pos+1,p);
    } 
    public static void FCFS(){
        int time = 0;
        int sum = 0;
        int avg;
        newQueue = new ArrayList<Process>();
        readyQueue = new ArrayList<Process>();
        waitQueue = new ArrayList<Process>();
        Process curRunning = null;
        resetProcs();
        while(processesRunning()){
        
            for (Process p : Processes)
                if (p.getArrivalTime() == time)
                {
                    if( readyQueue.size() < MP)
                        readyQueue.add(p);
                    else
                        if(!newQueue.contains(p))
                            newQueue.add(p);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                readyQueue.add(newQueue.remove(0));
            }
            
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
        for(Process p : Processes)
            sum+= p.getWaitingTime();
        System.out.println(sum/Processes.size());
    }
    public static void RR(){
            int time = 0;
            int sum = 0;
            int avg;
        readyQueue = new ArrayList<Process>();
        waitQueue = new ArrayList<Process>();
        newQueue = new ArrayList<Process>();
        Process curRunning = null;
        resetProcs();
        
        while(processesRunning()){
        
            for (Process p : Processes)
                if (p.getArrivalTime() == time)
                {
                    if( readyQueue.size() < MP)
                        readyQueue.add(p);
                    else
                        if(!newQueue.contains(p))
                            newQueue.add(p);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                readyQueue.add(newQueue.remove(0));
            }
            
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
        for(Process p : Processes)
            sum+= p.getWaitingTime();
        System.out.println(sum/Processes.size());
    }
    public static void PN(){
        int time = 0;
        readyQueue = new ArrayList<Process>();
        waitQueue = new ArrayList<Process>();
        newQueue = new ArrayList<Process>();
        Process curRunning = null;
        resetProcs();

        while(processesRunning()){

            for (Process p : Processes)
                if (p.getArrivalTime() == time)
                {
                    if( readyQueue.size() < MP)
                        insertSorted(p,readyQueue);
                    else
                        if(!newQueue.contains(p))
                            insertSorted(p,newQueue);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                readyQueue.add(newQueue.remove(0));
            }
            
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
    public static void PP(){
        int time = 0;
        readyQueue = new ArrayList<Process>();
        waitQueue = new ArrayList<Process>();
        newQueue = new ArrayList<Process>();
        Process curRunning = null;
        resetProcs();

        while(processesRunning()){

            for (Process p : Processes)
                if (p.getArrivalTime() == time)
                {
                    if( readyQueue.size() < MP)
                        insertSorted(p,readyQueue);
                    else
                        if(!newQueue.contains(p))
                            insertSorted(p,newQueue);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                readyQueue.add(newQueue.remove(0));
            }
            
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
            
            //Process queue preemptively
            if(!readyQueue.isEmpty() && curRunning != null && 
               readyQueue.get(0).getPriority() > curRunning.getPriority())
            {
                Process temp = null;
                temp = curRunning;
                curRunning = readyQueue.remove(0);
                insertSorted(temp,readyQueue);
                temp = null;
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
}
