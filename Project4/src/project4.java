import java.io.*;
import java.util.*;

public class project4 {

    public static void main(String[] args) throws IOException {

        //long inputstart = System.nanoTime();

        //String case_num = "13";
        //File inputFile = new File("src/largeCases/input/stressPart1-2.txt");
        //File inputFile = new File("src/smallCases/input/inp"+case_num+".txt");
        //File inputFile = new File("CustomInputs/input6-1H.in");
        //File outputFileTrue = new File("src/largeCases/output/stressPart1Out2.txt");
        //File outputFileTrue = new File("src/smallCases/output/out"+case_num+".txt");
        //File outputFile = new File("outputum.txt");

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        //File inputFile = new File("/Users/ardasaygan/Desktop/stress1.txt");
        //File outputFile = new File("/Users/ardasaygan/Desktop/outputum.txt");

        Reader inputFileReader = new FileReader(inputFile);
        //Scanner sc = new Scanner(outputFileTrue);
        BufferedReader reader = new BufferedReader(inputFileReader);
        FileWriter wr = new FileWriter(outputFile);

        int V = Integer.parseInt(reader.readLine());
        int M = Integer.parseInt(reader.readLine());

        String[] x = reader.readLine().split(" ");
        String startingName = x[0]; ///////
        String endingName = x[1]; ///////

        HashSet<String> flagNames = new HashSet<>(10*M);
        String[] flagNamesArr = reader.readLine().split(" ");
        Collections.addAll(flagNames, flagNamesArr);

        HashMap<String, ArrayList<Tuple<String, Integer>>> adjList = new HashMap<>(10*V, 0.1f); ///////
        HashMap<String, Integer> NodeDistances = new HashMap<>(5*V, 0.1f); //////

        String line;
        int t = 0;
        String[] nextLine;
        String nodeName;
        while ((line = reader.readLine()) != null) {
            /*if (t++%100 == 0){
                System.out.println(t/1000000.0);
            }*/
            nextLine = line.split(" ");
            nodeName = nextLine[0];
            NodeDistances.put(nodeName, Integer.MAX_VALUE);

            for (int i = 1; i < nextLine.length - 1; i += 2) {
                String adjNodeName = nextLine[i];
                int adjDistance = Integer.parseInt(nextLine[i + 1]);

                adjList.computeIfAbsent(nodeName, k -> new ArrayList<>());
                adjList.get(nodeName).add(new Tuple<>(adjNodeName, adjDistance));

                adjList.computeIfAbsent(adjNodeName, k -> new ArrayList<>());
                adjList.get(adjNodeName).add(new Tuple<>(nodeName, adjDistance));
            }
        }
        //long inputend = System.nanoTime();
        //System.out.println("input time :  " + (inputend - inputstart) / 1000000000.0 );


        // INPUTS ARE TAKEN
        //long start = System.nanoTime();

        // DIJKSTRA FOR FLAGS
        PriorityQueue<Tuple<String, Integer>> pq = new PriorityQueue<>();
        int flagCount = M;
        ArrayList<Integer> flagDistances = new ArrayList<>();
        HashSet<String> visitedNodes = new HashSet<>(10*V, 0.1f);
        HashSet<String> visitedFlags = new HashSet<>();

        // add the starter node to pq
        String starterFlagName = flagNamesArr[0];
        //int starterFlagCode = flagCode;
        NodeDistances.put(starterFlagName, 0);
        pq.add(new Tuple<>(starterFlagName, 0));

        while (flagCount > 0 && !pq.isEmpty()) {
            Tuple<String, Integer> currNodeTuple = pq.poll();
            String currName = currNodeTuple.first;
            Integer currDistance = currNodeTuple.second;

            if (visitedNodes.contains(currName)) {
                continue;
            }

            if (flagNames.contains(currName)) {
                if (visitedFlags.contains(currName)) {
                    continue;
                }

                visitedNodes.clear();

                visitedFlags.add(currName);
                visitedNodes.add(currName);

                flagDistances.add(currDistance);
                flagCount--;

                if (adjList.get(currName) == null) {
                    continue;
                }
                for (Tuple<String, Integer> tuple : adjList.get(currName)) {
                    String adjName = tuple.first;
                    Integer adjDistance = tuple.second;

                    if (visitedFlags.contains(adjName)) {
                        continue;
                    }

                    if (NodeDistances.get(adjName) < adjDistance) {
                        continue;
                    }
                    NodeDistances.put(adjName, adjDistance);
                    pq.add(new Tuple<>(adjName, adjDistance));
                }
            } else {
                visitedNodes.add(currName);
                if (adjList.get(currName) == null) {
                    continue;
                }

                for (Tuple<String, Integer> tuple : adjList.get(currName)) {
                    String adjName = tuple.first;
                    Integer adjDistance = tuple.second;

                    if (visitedNodes.contains(adjName)) {
                        continue;
                    }

                    if (NodeDistances.get(adjName) < currDistance+ adjDistance) {
                        continue;
                    }
                    NodeDistances.put(adjName, currDistance + adjDistance);
                    pq.add(new Tuple<>(adjName, currDistance + adjDistance));
                }

            }

        }
        int flagcut = 0;
        if (flagCount > 0) {
            flagcut = -1;
            //System.out.println(-1);
        }
        else {
            //System.out.println("Print flag distances");
            int sum = 0;
            for (Integer num : flagDistances) {
                sum += num;
                //System.out.print(num + " ");
            }
            //System.out.println();
            //System.out.println("sum = " + sum);
            flagcut = sum;
        }

        // ...

       // long finish = System.nanoTime();
        //long flagTime = finish - start;
        //long dijkstraStart = System.nanoTime();
        int marathon = dijkstra(startingName, endingName, adjList, V);
        //long dijkstraEnd = System.nanoTime();
        //long dijkstraTime = dijkstraEnd - dijkstraStart;
        //System.out.println("Dijkstra : " + marathon);
        /*
        System.out.println("Flag Cut: " + (flagTime/1000000000.0));
        System.out.println("Dijkstra time : " + (dijkstraTime / 1000000000.0));
        System.out.println("total time : " + (flagTime +dijkstraTime+ (inputend - inputstart))/1000000000.0);

        System.out.println();
        System.out.println(">>>  "+marathon);
        System.out.println(">>>  "+flagcut);

        System.out.println("ttt  "+sc.nextLine());
        System.out.println("ttt  "+sc.nextLine()); */


        wr.write(Integer.toString( marathon)+"\n");
        wr.write(Integer.toString(flagcut));
        wr.flush();
        wr.close();
    }


    public static int dijkstra(String start, String end, HashMap<String, ArrayList<Tuple<String, Integer>>> adj_list, int V) {

        PriorityQueue<Tuple<String, Integer>> pq = new PriorityQueue<>(); // nodeCode, distance
        HashMap<String, Integer> distances = new HashMap<>(10*V, 0.1f);

        HashSet<String> visited = new HashSet<>(V);
        pq.add(new Tuple<>(start, 0));
        distances.put(start, 0);

        while (!pq.isEmpty()) {

            Tuple<String, Integer> curr = pq.poll();

            String currName = curr.first;
            int currDistance = curr.second;

            if (visited.contains(currName)){
                continue;
            }
            visited.add(currName);

            if(currName.equals(end)){
                return currDistance;
            }

            for (Tuple<String , Integer> adjTuple : adj_list.get(currName)) {
                String adjName = adjTuple.first;
                Integer adjDistance = adjTuple.second;

                if (visited.contains(adjName)) {
                    continue;
                }

                if (currDistance + adjDistance > distances.getOrDefault(adjName, Integer.MAX_VALUE)){
                    continue;
                }
                distances.put(adjName, currDistance + adjDistance);
                pq.add(new Tuple<>(adjName,currDistance + adjDistance));
            }
        }

        return -1;
    }
}