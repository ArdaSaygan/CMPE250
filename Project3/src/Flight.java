import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Flight implements Comparable<Flight>{
    public int admissionTime;
    public String flightCode;
    public String accCode;
    public String depAirCode;
    public String arrAirCode;
    public int[] operationTimes;

    public int currOperation;
    public Set<Integer> accOperations;
    public boolean newToQueue;
    public boolean onAir;

    // used while reporting, the time when flight is put to the ready queue
    public int reconsiderationTime;
    public boolean willStratNewOp;

    public Flight(int admissionTime,String flightCode,String accCode,String depAirCode,String arrAirCode,int[] operationTimes){
        this.admissionTime = admissionTime;
        this.flightCode = flightCode;
        this.accCode = accCode;
        this.depAirCode = depAirCode;
        this.arrAirCode = arrAirCode;
        this.operationTimes = operationTimes;

        this.currOperation = 0;
        this.accOperations = new HashSet<Integer>(Arrays.asList(0, 1, 2, 10, 11, 12, 20));
        this.newToQueue = true;
        this.onAir = false;
        this.willStratNewOp = true;
    }

    @Override
    public int compareTo(Flight otherFlight) {
        if (this.admissionTime < otherFlight.admissionTime){
            return -1;
        }
        else if (this.admissionTime > otherFlight.admissionTime){
            return 1;
        }
        else{
            if (this.newToQueue && !otherFlight.newToQueue){
                return -1;
            }
            else if (!this.newToQueue && otherFlight.newToQueue){
                return 1;
            }
            else {
                return this.flightCode.compareTo(otherFlight.flightCode);
            }
        }
    }

    /**
     * decrements an operation time by one. Updates the currOperation if necessary
     * @return 2 if the next operation will be ACC, 1 if ATC, -1 if it will terminate, 0 if the operation is not complete yet.
     */
    public int operate(){
        if (operationTimes[currOperation] != 0){
            operationTimes[currOperation] = operationTimes[currOperation] - 1;
        }

        willStratNewOp = false;

        if (operationTimes[currOperation] == 0){
            currOperation++;
            willStratNewOp = true;
        }
        else {
            return 0;
        }


        if (currOperation >= 21){
            return -1;
        }
        if (accOperations.contains(currOperation)){
            return 2;
        }
        else {
            return 1;
        }
    }

    public int skipOperate(int byThisTime){
        if (operationTimes[currOperation] < byThisTime){
            System.out.println("THERE IS A PROBLEM");
        }

        if (operationTimes[currOperation] != 0){
            operationTimes[currOperation] = operationTimes[currOperation] - byThisTime;
        }

        willStratNewOp = false;

        if (operationTimes[currOperation] == 0){
            currOperation++;
            willStratNewOp = true;
        }
        else {
            return 0;
        }


        if (currOperation >= 21){
            return -1;
        }
        if (accOperations.contains(currOperation)){
            return 2;
        }
        else {
            return 1;
        }
    }

    /**
     * To write report logs, use this method w
     * @param time, should the time when this flight started an operation
     */
    public ArrayList<String> report(int time, ArrayList<String> reportList){


        if (!willStratNewOp){ // only report when starting a new operation
            return reportList;
        }

        if (currOperation >= 21){
            String text = reconsiderationTime + " | " + time + " | " + flightCode + " | " + "Finished" + " | " + 0;
            //System.out.println(text);
            reportList.add(text);
            return reportList;
        }
        String currOperationCode = "";
        if (accOperations.contains(currOperation)){
            if (currOperation == 1 || currOperation == 11){ // acc waiting
                currOperationCode = accCode + " Waiting";
            }
            else{ // acc running
                currOperationCode = accCode + " Running";
            }
        }
        else if (Arrays.asList(4, 6, 8).contains(currOperation)){ // depAtc waiting
            currOperationCode = depAirCode + " Waiting";
        }
        else if (Arrays.asList(3, 5 ,7 ,9).contains(currOperation)){ // depAtc running
            currOperationCode = depAirCode + " Running";
        }
        else if (Arrays.asList(14, 16, 18).contains(currOperation)){ // arrAtc waiting
            currOperationCode = arrAirCode + " Waiting";
        }
        else if (Arrays.asList(13, 15, 17, 19).contains(currOperation)){ // arrAtc running
            currOperationCode = arrAirCode + " Running";
        }
        String text = reconsiderationTime + " | " + time + " | " + flightCode + " | " + currOperationCode + " | " + operationTimes[currOperation];
        //System.out.println(text);
        reportList.add(text);
        return reportList;
    }
}
