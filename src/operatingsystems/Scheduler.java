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
    public static int cpuUt = 0;
    public static int ioUt = 0;
    public static int tp=0;
    
    
    public void Scheduler(){
    }
    
    public static void clear(){
        Scheduler.Processes.clear();
        Process.ProcessNumber = 0;
    }

    public static int getMP() {
        return MP;
    }

    public static void setMP(int MP) {
        Scheduler.MP = MP;
    }
    
    public static void resetProcs(){
        cpuUt = 0;
        ioUt = 0;
        tp=0;
        for(Process p : Processes)
            p.resetProcess();
    }
    
    public static float getUt(){
        return ((float)cpuUt/(float)(ioUt+cpuUt))*100;
    }
    public static boolean processesRunning() { // function to check if all process has finished or not
            for (Process p : Processes)
                    if (p.getFinishTime() == -1) // if any process has finish time = -1, then it hasn't finished yet
                            return true;
            return false;
    }
    
    public static float calcAverageWait(){
        float sum = 0;
        int avg;
        for(Process p: Processes)
        {
            sum += p.getWaitingTime();
        }
        return sum/Processes.size();
    }
    public static float calcAverageTurnAround(){
        float sum = 0;
        int avg;
        for(Process p: Processes)
        {
            sum += p.getTurnAround();
        }
        return sum/Processes.size();
    }
    
    public static int getThroughput(){
        return tp;
        
    }
    
    public static void insertPriority(Process p,ArrayList<Process> arr){
        Comparator<Process> comp = Comparator.comparing(process -> process.getPriority());
        comp = comp.thenComparing(Comparator.comparing(process -> process.getArrivalTime()));
        int pos = Collections.binarySearch(arr,p,comp);
        if (pos < 0) {
            arr.add(-pos-1, p);
        }
        else
            arr.add(pos+1,p);
    } 
    public static void insertShortestRemaining(Process p,ArrayList<Process> arr){
        Comparator<Process> comp = Comparator.comparing(process -> process.getRemainingTimeCPU());
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
                ioUt++;
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
                    cpuUt++;
                    curRunning.decCPU();
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
                        curRunning = null;
                    }
                }
            }
            time++;
        }
        System.out.println("CPU:"+cpuUt);
        System.out.println("IO:"+ioUt);
        System.out.println("UT:"+getUt());
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
                 ioUt++;
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
                    cpuUt++;
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
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
                        insertPriority(p,readyQueue);
                    else
                        if(!newQueue.contains(p))
                            insertPriority(p,newQueue);
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
                    ioUt++;
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
                    cpuUt++;
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
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
                        insertPriority(p,readyQueue);
                    else
                        if(!newQueue.contains(p))
                            insertPriority(p,newQueue);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                insertPriority(newQueue.remove(0),readyQueue);
            }
            
            if(!waitQueue.isEmpty())
            {
                Iterator<Process> iter = waitQueue.iterator();
                ioUt++;
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
               readyQueue.get(0).getPriority() < curRunning.getPriority())
            {
                insertPriority(curRunning, readyQueue);
                curRunning = null;
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
                    cpuUt++;
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
                        curRunning = null;
                    }
                }
            }
            time++;
        }
    }
    public static void SJF(){
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
                        insertShortestRemaining(p,readyQueue);
                    else
                        if(!newQueue.contains(p))
                            insertShortestRemaining(p,newQueue);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                insertShortestRemaining(newQueue.remove(0),readyQueue);
            }
            
            if(!waitQueue.isEmpty())
            {
                Iterator<Process> iter = waitQueue.iterator();
                ioUt++;
                while(iter.hasNext())
                {
                    Process p = iter.next();
                    p.decIO();
                    
                    if(p.getRemainingTimeIO() <= 0)
                    {
                        insertShortestRemaining(p, readyQueue);
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
                    cpuUt++;
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
                        curRunning = null;
                    }
                }
            }
            time++;
        }
    }
    public static void SRTF(){
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
                        insertShortestRemaining(p,readyQueue);
                    else
                        if(!newQueue.contains(p))
                            insertShortestRemaining(p,newQueue);
                } //Keep checking if we can add
            
            while(readyQueue.size() < MP && !newQueue.isEmpty())
            {
                insertShortestRemaining(newQueue.remove(0),readyQueue);
            }
            
            if(!waitQueue.isEmpty())
            {
                Iterator<Process> iter = waitQueue.iterator();
                ioUt++;
                while(iter.hasNext())
                {
                    Process p = iter.next();
                    p.decIO();
                    
                    if(p.getRemainingTimeIO() <= 0)
                    {
                        insertShortestRemaining(p, readyQueue);
                        iter.remove();
                    }
                }
            }
            
            if(!readyQueue.isEmpty()  && curRunning != null &&
                        readyQueue.get(0).getRemainingTimeCPU() < curRunning.getRemainingTimeCPU())
            {
                insertShortestRemaining(curRunning, readyQueue);
                curRunning = null;
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
                    cpuUt++;
                    if(curRunning.getRemainingTimeCPU() == 0)
                    {
                        System.out.print("| P("+curRunning.getPid()+") | ");
                        curRunning.setFinishTime(time+1);
                        curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
                        curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
                        if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
                        curRunning = null;
                    }
                }
            }
            time++;
        }
    }
    public static void SRTFbkup() {
		int time = 0; // current time
		Process curRunning = null; 
		readyQueue = new ArrayList<Process>(); 
                waitQueue = new ArrayList<Process>();
                newQueue = new ArrayList<Process>();

		int minimumRem = Integer.MAX_VALUE;
		Process shortestRem = null;
        
        while(processesRunning()){
            
            for (Process p : Processes)
                if (p.getArrivalTime() == time && readyQueue.size() < MP)
                {
                	if( readyQueue.size() < MP)
                            
                            readyQueue.add(p);
                        else
                            if(!newQueue.contains(p)){
                                newQueue.add(p);
                            }
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
            
            // decrement its remaining running time
            
		for (Process p : readyQueue) {
                    if (p.getRemainingTimeCPU() < minimumRem) 
                    { // if process p has less remaining running time than
                        shortestRem = p; // make p the leastRemainingTimeProcess
                        minimumRem = p.getRemainingTimeCPU();
                    }
		}
                
                if(shortestRem != null){
                    readyQueue.add(curRunning);
                    curRunning= shortestRem;	
                }
                
		if (curRunning == null)
                {
                 
                    if(readyQueue.size() > 0)
                        {
                            curRunning = readyQueue.remove(0);
                        
                            if(curRunning.getStartTime() == -1)
                                
                                curRunning.setStartTime(time);
                        }	
                    }
                
                
			
		if (curRunning != null) 
		{ 
				//Work calculations!
                                //First Check if process asks for IO!
					
                    if(curRunning.getReqIO() != -1 && curRunning.getRemainingTimeIO() > 0 &&
                       curRunning.getReqIO()+curRunning.getRemainingTimeCPU() == curRunning.getCpuBurst())
                        {
			    waitQueue.add(curRunning);
			    curRunning = null;
                        }
			               
                    else//If not, do the CPU math!
                    {
			curRunning.decCPU();             // decrement its remaining running time

			if(curRunning.getRemainingTimeCPU() == 0)
			{
			    System.out.print("| P("+curRunning.getPid()+") | ");
			    curRunning.setFinishTime(time);
			    curRunning.setTurnAround(curRunning.getFinishTime() - curRunning.getStartTime());
			    curRunning.setWaitingTime(curRunning.getTurnAround() - curRunning.getCpuBurst());
			                        
			    int shortestRemRunning = readyQueue.indexOf(curRunning); // find the index of the finished
			    readyQueue.remove(shortestRemRunning); // remove the finished process from the readyQueue
                            if (curRunning.getFinishTime()<= 1000)
                            tp+=1;
			    curRunning = null;
                        }
                        else
                        {
                            readyQueue.add(curRunning);
                            curRunning = null;
                        }
                    }
                }
		time++; // increment time	     	
            }
				
	
        }
}







