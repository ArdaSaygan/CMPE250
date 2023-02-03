import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ATC {
    private int time;
    // 7 capital letters = accCode(4) + number(3)
    public String atcCode;
    public String airportCode;
    public String hashOfairportCode;

    PriorityQueue<Flight> flightsATC = new PriorityQueue<>();
    Queue<Flight> ready2 = new LinkedList<>();

    public ATC(String accCode, String airportCode, int hash){
        String hashString = Integer.toString(hash);

        if (hashString.length() == 2){
            hashString = "0" + hashString;
        } else if (hashString.length() == 1) {
            hashString = "00" + hashString;
        }

        atcCode = accCode + hashString;
        this.airportCode = airportCode;
        this.hashOfairportCode = hashString;
    }

    public void updateTime(int time){
        this.time = time;
    }

    /**
     *
     * @return false if flightsATC is empty, true if it's not
     */
    public boolean checkFlightsATC(){
        if (flightsATC.isEmpty()){
            return false;
        }
        while (flightsATC.peek() != null && flightsATC.peek().admissionTime <= time){
            Flight peekFlight = flightsATC.poll();
            peekFlight.reconsiderationTime = time;
            ready2.add(peekFlight);
        }
        return true;
    }

}
