import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ACC {
    private int time;


    public String accCode; // 4 capital letters
    public ATC[] atcTable; // size 1000
    public HashMap<String, Integer> keyToKeyAtcTable = new HashMap<>();
    public TreeSet<Integer> atcTableIndexes = new TreeSet<>();
    public int lastTerminationTime; // should be updated whenever a flight terminates
    PriorityQueue<Flight> flights = new PriorityQueue<>();
    Queue<Flight> ready1 = new LinkedList<>();
    private int ready1Clock; // counter for ready1 queue

    public ArrayList<String> reportList = new ArrayList<>();

    public List<Integer> skipTimes = new ArrayList<>();
    public ACC(String accCode){
        this.accCode = accCode;
        atcTable = new ATC[1000];
        Arrays.fill(atcTable, null);
        this.time = 0;
        lastTerminationTime = 0;
        ready1Clock = 30;
    }

    /**
     * Finds a new position for the given element key.
     * @param airportCode
     * @return the index to be inserted
     */
    public int hash(String airportCode){
        // x will be the new position in the table
        int x = ((int)airportCode.charAt(0));
        x +=  Math.pow(31,1)*((int)airportCode.charAt(1)) + Math.pow(31,2)*((int)airportCode.charAt(2));
        x = x % 1000;
        while (atcTable[x] != null){
            x = x + 1;
            if (x == 1000){
                x=0;
            }
        }
        return x;
    }

    public void addToHashTable(String airportCode){
        int hash = hash(airportCode);
        ATC atc = new ATC(accCode, airportCode, hash);
        atcTable[hash] = atc;
        atcTableIndexes.add(hash);
        keyToKeyAtcTable.put(airportCode, hash);
    }

    /**
     * Finds the ATC with the given airportCode in the table
     * @param airportCode
     * @return ATC with the given airport code, null if no such atc exist.
     */
    public ATC findATCinTable(String airportCode){
        /*
        int x = ((int)airportCode.charAt(0));
        x +=  Math.pow(31,1)*((int)airportCode.charAt(1)) + Math.pow(31,2)*((int)airportCode.charAt(2));
        x = x % 1000;
        while (atcTable[x] != null && !atcTable[x].airportCode.equals(airportCode)){
            x = x + 1;
            if (x == 1000){
                x=0;
            }
        }
        return atcTable[x]; */

        return atcTable[keyToKeyAtcTable.get(airportCode)];
    }

    public void updateTime(int time){
        this.time = time;
    }

    /**
     * Look at the peek in Flights PQ, if it's time put it to the ready queue. Do this again for the new peek. Stop if it's not time.
     * @return false if flightsPQ was empty when this function was called, true if it was not.
     */
    public boolean checkFlights(){

        if (flights.isEmpty()){
            return false;
        }

        while (flights.peek() != null && flights.peek().admissionTime == time){
            Flight peekFlight = flights.poll();
            peekFlight.reconsiderationTime = time;
            ready1.add(peekFlight);
        }
        return true;
    }

    /**
     *
     * @return false if ready1 was empty when this function was called, true if it was not.
     */
    public boolean checkReady1(){


        if (ready1.peek() == null){
            return false;
        }

        //reportList = ready1.peek().report(time, reportList);
        int reportCode = ready1.peek().operate();
        ready1Clock--;

        if (reportCode == 0 && ready1Clock>0){
            return true;
        }
        if (reportCode == 0){ //&& ready1Clock <= 0
            Flight peekFlight = ready1.poll();
            peekFlight.reconsiderationTime=time+1;
            peekFlight.willStratNewOp=true;
            peekFlight.newToQueue = false;
            // ready1.add( peekFlight );
            peekFlight.admissionTime = time + 1;
            flights.add(peekFlight);

            ready1Clock = 30;
        }
        if (reportCode == 2){
            Flight peekFlight = ready1.poll();
            ready1Clock = 30;
            peekFlight.reconsiderationTime = time + 1;
            //reportList = peekFlight.report(time + 1, reportList); // TO REPORT THE WAITING OPERATIONS
            peekFlight.newToQueue = true; // leaves the ready1 queue
            peekFlight.admissionTime = time + peekFlight.operationTimes[peekFlight.currOperation] + 1; // currTime + waitingTime + 1
            peekFlight.operationTimes[peekFlight.currOperation] = 0;
            peekFlight.currOperation++;
            flights.add(peekFlight);

        }
        if (reportCode == 1){
            Flight peekFlight = ready1.poll();
            peekFlight.admissionTime = time + 1; // SO THAT ACCS LAST OPERATION AND ATCS FIRST OPERATION WILL NOT BE AT THE SAME TIME
            ready1Clock = 30;

            peekFlight.newToQueue = true; // Finally leaves the ready1 queue so reset this.

            // find its ATC in the table, and add it to that ATC. You may come up with new methods in ATC
            // this flight should be added to the relevant ATC's flightATC PQ

            // if it's already in air, it should go to the arrival airport atc
            if (peekFlight.onAir){
                // go to the arrival airport
                ATC atc = findATCinTable(peekFlight.arrAirCode);
                atc.flightsATC.add(peekFlight);
                peekFlight.onAir = false;
            }
            else{
                // go to the destination airport
                ATC atc = findATCinTable(peekFlight.depAirCode);
                atc.flightsATC.add(peekFlight);
                peekFlight.onAir = true;
            }
        }

        if (reportCode == -1){
            Flight peekFlight = ready1.poll();
            peekFlight.reconsiderationTime = time + 1;
            //reportList = peekFlight.report(time + 1, reportList);
            ready1Clock = 30;
            lastTerminationTime = time + 1;


        }

        return true;
    }

    /**
     *
     * @return false if both flightsATC and ready2 in all atcs are empty, true otherwise
     */
    public boolean checkAtcs(){
        boolean someATCnotEmpty = false;
        // for atcs in the table
        for (Integer i : atcTableIndexes) {
            ATC atc = atcTable[i];

            // update their time
            atc.updateTime(time);

            boolean fatcpq = atc.checkFlightsATC();

            boolean r2q = chechkReady2(atc);

            if (fatcpq || r2q){
                someATCnotEmpty = true;
            }
        }

        return someATCnotEmpty;
    }

    /**
     * helper function for checkAtcs
     *
     * operate, according to its report code, take action
     * if next operation is acc, poll ready2 queue and add it to ready1
     * @return false if ready2 was empty when called, true if it was not.
     */
    private boolean chechkReady2(ATC atc){
        Queue<Flight> ready2 = atc.ready2;
        if (ready2.peek() == null){
            return false;
        }



        //reportList = ready2.peek().report(time, reportList);
        int reportCode = ready2.peek().operate();

        if (reportCode == 0){
            // do nothing
            return true;
        }
        if (reportCode == 1){
            Flight peekFlight = ready2.poll();
            peekFlight.reconsiderationTime = time + 1;
            //reportList = peekFlight.report(time + 1, reportList); // TO REPORT THE WAITING OPERATION
            peekFlight.admissionTime = time + peekFlight.operationTimes[peekFlight.currOperation] + 1; // currTime + waitingTime + 1
            peekFlight.operationTimes[peekFlight.currOperation] = 0;
            peekFlight.currOperation++;
            atc.flightsATC.add(peekFlight);
        }
        if (reportCode == 2){
            Flight peekFlight = ready2.poll();
            peekFlight.reconsiderationTime = time+1;
            //ready1.add(peekFlight);
            peekFlight.admissionTime = time+1;
            flights.add(peekFlight);
        }

        return true;
    }

    public int skipTo(){
        int skipTime = -1;

        skipTimes.clear();

        if (!flights.isEmpty()){
            skipTimes.add(flights.peek().admissionTime - 1);
        }

        if (!ready1.isEmpty()){
            int r = Math.min(ready1.peek().operationTimes[ready1.peek().currOperation] - 1, ready1Clock - 1);
            skipTimes.add(time + r);
            //skipTimes.add(-1);
        }


        // for every atc
        for (int index : atcTableIndexes) {
            ATC atc = atcTable[index];

            if (!atc.flightsATC.isEmpty()){
                skipTimes.add(atc.flightsATC.peek().admissionTime - 1);
                //skipTimes.add(-1);
            }
            if (!atc.ready2.isEmpty()){
                int r = atc.ready2.peek().operationTimes[atc.ready2.peek().currOperation] - 1;
                skipTimes.add(time + r);
                //skipTimes.add(-1);
            }
        }

        if (!skipTimes.isEmpty()){
            skipTime = Collections.min(skipTimes);
        }

        return skipTime;
    }

    public void operateInSkipping(int byThisTime){
        // operate Ready1 peek
        if (!ready1.isEmpty()){
            ready1.peek().skipOperate(byThisTime);
            ready1Clock -= byThisTime;
        }

        // operate every atcs ready2 peek
        for (int index : atcTableIndexes) {
            ATC atc = atcTable[index];
            if (!atc.ready2.isEmpty()){
                atc.ready2.peek().skipOperate(byThisTime);
            }
        }
    }

    public void updateAllTime(int byTime){
        this.time = byTime;
        for (int index : atcTableIndexes) {
            atcTable[index].updateTime(byTime);
        }
    }


}
