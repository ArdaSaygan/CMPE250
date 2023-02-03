import java.io.*;
import java.util.*;

public class Project3 {



    public static void main(String[] args) throws IOException {

        ArrayList<String> outputStrings = new ArrayList<>();


        /*
        READ THE INPUTS AND PUT THE INTO LISTS
         */
        int casen = 4;
        //File inputFile = new File("case18.in");
        //FileReader inputFileReader = new FileReader("newCases/inputs2/case0.in");
        //File outputFile = new File("outputum.out");
        //Writer outputFileWriter = new FileWriter("outputum.out");
        //File logFile = new File("cases/logs/case"+casen+".log");

        //File inputFile = new File(args[0]);
        Reader inputFileReader = new FileReader(args[0]);
        //File outputFile = new File(args[1]);
        Writer outputFileWriter = new FileWriter(args[1]);

        //File mylogFile = new File("logum.log");
        //File origlogFile = new File("origLog.log");
        BufferedReader sc = new BufferedReader(inputFileReader);
        BufferedWriter wr = new BufferedWriter(outputFileWriter);

        //String[] firstLine = sc.nextLine().split(" ");
        String[] firstLine = sc.readLine().split(" ");
        int A = Integer.parseInt(firstLine[0]);
        int F = Integer.parseInt(firstLine[1]);

        // Data Structures
        HashMap<String, ACC>  accs = new HashMap<>();


        // read the accs and airport codes
        for (int i = 0; i < A; i++) {
            String[] nextLine = sc.readLine().split(" ");
            String accCode = nextLine[0];
            ACC acc = new ACC(accCode);
            // add the airport codes to table
            for (int j = 1; j < nextLine.length; j++) {
                acc.addToHashTable(nextLine[j]);
            }
            accs.put(accCode, acc);
        }

        // read the info about the flights
        for (int i = 0; i < F; i++) {
            String[] nextLine = sc.readLine().split(" ");
            int admissionTime = Integer.parseInt(nextLine[0]);
            String flightCode = nextLine[1];
            String accCode = nextLine[2];
            String depAirCode = nextLine[3];
            String arrAirCode = nextLine[4];

            int[] operationTimes = new int[21];
            for (int j = 5; j < nextLine.length; j++){
                operationTimes[j-5] = Integer.parseInt(nextLine[j]);
            }

            Flight flight = new Flight(admissionTime, flightCode, accCode, depAirCode, arrAirCode, operationTimes);

            // add this flight to the right acc's flights PQ
            accs.get(accCode).flights.add(flight);
        }

        //System.out.println("Finished reading the inputs");

        /*
         NOW THE FUNCTIONAL PART COMES
         */

        /*
        int time = -1;
        boolean allEmpty = false;
        while (!allEmpty){ // while not allEmpty
            time++;
            allEmpty = true;
            if(time == 1000000){
                int a = 1;
            }
            for (ACC acc : accs.values()) {
                acc.updateTime(time);

                boolean fpq = acc.checkFlights();
                boolean r1q = acc.checkReady1();

                boolean atcs = acc.checkAtcs();

                if (fpq || r1q || atcs){
                    allEmpty = false; // not all of the pqs and queues in accs and their atcs are empty
                }
            }
        } */

        // for every acc, perform one by one
        int accNum = 1;
        for (ACC acc : accs.values()) {
            //System.out.println(accNum++);

            int time = -1;
            boolean allEmpty = false;

            while (!allEmpty){ // while not allEmpty

                int skipToTime = acc.skipTo();
                if (time < skipToTime){
                    // operate ready1 and ready2 by (SkipToTime - time)
                    acc.operateInSkipping(skipToTime - time );
                    time = skipToTime;
                    acc.updateAllTime(time);
                    continue;
                }

                time++;


                allEmpty = true;

                acc.updateTime(time);

                boolean fpq = acc.checkFlights();
                boolean r1q = acc.checkReady1();

                boolean atcs = acc.checkAtcs();

                if (fpq || r1q || atcs){
                    allEmpty = false; // not all of the pqs and queues in accs and their atcs are empty
                }
            }


            printAcc(acc, wr, outputStrings);
        }

        wr.flush();

        /*
        NOW THE OUTPUT PART COMES
         */
        //System.out.println("Starting to write the outputs");

        /*for (ACC acc : accs.values()) {
            ArrayList<String> outputStrings = new ArrayList<>();
            outputStrings.add(acc.accCode);
            outputStrings.add( Integer.toString(acc.lastTerminationTime));

            for (int i : acc.atcTableIndexes) {
                outputStrings.add( acc.atcTable[i].airportCode + acc.atcTable[i].hashOfairportCode);
            }

            wr.write(outputStrings.get(0));
            for (int i = 1; i < outputStrings.size(); i++) {
                wr.write( " " + outputStrings.get(i));
            }

            wr.write("\n");
            wr.flush();
        }*/
    
        
        // now for the log file
        //ArrayList<String> allLogs = new ArrayList<>();
        //for (ACC acc : accs.values()) {
            //allLogs.addAll(acc.reportList);
        //}
        //Collections.sort(allLogs);
        //FileWriter logWriter = new FileWriter(mylogFile);
        //for (String line : allLogs) {
        //    logWriter.write(line + "\n");
        //}

        // also sort the original log file
        //ArrayList<String> origsLogs = new ArrayList<>();
        //Scanner scLogs = new Scanner(logFile);
        //FileWriter origlogWriter = new FileWriter(origlogFile);
        //while (scLogs.hasNext()){
        //    origsLogs.add(scLogs.nextLine());
        //}
        //Collections.sort(origsLogs);
        //for (String line : origsLogs) {
        //    origlogWriter.write(line+"\n");
        //}



    }

    public static void printAcc(ACC acc, BufferedWriter wr, ArrayList<String> outputStrings) throws IOException {

        outputStrings.clear();
        outputStrings.add(acc.accCode);
        outputStrings.add( Integer.toString(acc.lastTerminationTime));

        for (int i : acc.atcTableIndexes) {
            outputStrings.add( acc.atcTable[i].airportCode + acc.atcTable[i].hashOfairportCode);
        }

        wr.write(outputStrings.get(0));
        for (int i = 1; i < outputStrings.size(); i++) {
            wr.write( " " + outputStrings.get(i));
        }

        wr.write("\n");

    }
}