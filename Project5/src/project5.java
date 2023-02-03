import java.io.*;
import java.util.*;

public class project5 {

    static int flow;
    static int currBottleneck;
    static Hashtable<String, Node> nodes;

    static String KL = "KL";

    public static void main(String[] args) throws IOException {
        flow = 0;
        currBottleneck = Integer.MAX_VALUE;

        File input = new File(args[0]);
        File output = new File(args[1]);

        //File input = new File("src/updated_test_cases/input_30000.txt");
        //File input = new File("milyonluk.txt");
        //File output = new File("outputum.txt");

        FileWriter wr = new FileWriter(output);

        FileReader inputFileReader = new FileReader(input);
        BufferedReader reader = new BufferedReader(inputFileReader);

        nodes = new Hashtable<>();

        int no_cities = Integer.parseInt(reader.readLine());

        // add the entrances and regions
        String[] region_capacities = reader.readLine().split(" ");
        ArrayList<Node> entrNodes = new ArrayList<>();
        for (int i = 0; i <region_capacities.length ; i++) {
            String[] regAdjArr = reader.readLine().split(" ");

            // initilize the region node
            String regName = regAdjArr[0];
            Node regNode =  new Node(regName);
            nodes.put(regName, regNode);


            // add an entrance node before every city,
            String entrName = "entr_"+regName;
            Node entrNode = new Node(entrName);
            entrNode.adj_list.push(new Trio(regNode, true, Integer.parseInt(region_capacities[i])));
            nodes.put(entrName, entrNode);
            entrNodes.add(entrNode);

            // add the cities
            for (int j = 1; j < regAdjArr.length; j+=2) {
                // first check if this city is already initiliazed
                String cityName = regAdjArr[j];
                String adjDistance = regAdjArr[j+1];
                if (!nodes.containsKey(cityName)){
                    // initiliaze the city node
                    Node cityNode = new Node(cityName);
                    nodes.put(cityName, cityNode);

                }

                // make the connecetion with the region
                regNode.adj_list.push(new Trio(nodes.get(cityName), true, Integer.parseInt(adjDistance)));
            }
        }

        // add the rest of the cities
        String line;
        while ((line = reader.readLine()) != null) {
            String[] lineArr = line.split(" ");
            String cityName = lineArr[0];
            // check if the city node is already initiliazed
            if (!nodes.containsKey(cityName)){
                // initiliaze the city node
                Node cityNode = new Node(cityName);
                nodes.put(cityName,cityNode);
            }

            // add the adjCities
            for (int i = 1; i < lineArr.length ; i+=2) {
                String adjCityName = lineArr[i];
                String adjCityDistance = lineArr[i+1];

                // check if the city node is already initiliazed
                if (!nodes.containsKey(adjCityName)){
                    // initiliaze the city node
                    Node adjCityNode = new Node(adjCityName);
                    nodes.put(adjCityName,adjCityNode);

                }

                // make the connectin with the parent city
                nodes.get(cityName).adj_list.push(new Trio(nodes.get(adjCityName), true,Integer.parseInt(adjCityDistance)));
            }

        }


        // ALGORITHM TIME
        
        // find the MaxFlow
        boolean cont = true;
        while (cont){
            cont = false;
            for (Node entrnode : entrNodes) {
                if (BFS(entrnode)){
                    cont = true;
                }
            }
        }
        wr.write(flow+"\n");
        //System.out.println(flow);

        // find the MinCut

        // create the source
        Node source = new Node("source");
        nodes.put(source.name, source);
        for (Node entrNode: entrNodes) {
            source.adj_list.push(new Trio(entrNode, true, 1));
        }

        for (String s : minCut(source)) {
            if (s.split(" ")[0].contains("entr_")){
                s = s.split(" ")[1];
            }
            wr.write(s + "\n");
            //System.out.println(s);
        }

        wr.flush();



    }

    // DFS, will find a path on the residual graph, if weight is 0 then it will not use that edge
    // return True if there is a path
    public static boolean DFS(Node curr){
        Set<String> visited = new HashSet<>();
        currBottleneck = Integer.MAX_VALUE;
        boolean found = DFSHelper(curr, visited, Integer.MAX_VALUE); // will change the currBottleneck inside
        if (found){
            flow += currBottleneck;
        }

        
        return found;
    }

    public static boolean BFS(Node curr){
        Set<String> visited = new HashSet<>();
        currBottleneck = Integer.MAX_VALUE;
        boolean found = BFSHelper(curr, visited);
        if (found){
            flow += currBottleneck;
        }

        return found;
    }

    public static boolean BFSHelper(Node source, Set<String> visited){
        // parent list
        Hashtable<String, Trio> parentTrios = new Hashtable<>();
        Hashtable<String, Node> parentNodes = new Hashtable<>();
        // queue
        Queue<Trio> queue = new LinkedList<>();

        queue.add(new Trio(source, true, Integer.MAX_VALUE));
        visited.add(source.name);
        while (!queue.isEmpty()){
            // pop out of queue
            Trio queueTrio = queue.poll();
            Node curr = queueTrio.node;
            Integer minBottleneck = queueTrio.resCapacity;


            // add its nonvisited nodes to queue
            for (Trio adjTrio : curr.adj_list) {
                Node adjNode = adjTrio.node;
                Integer adjResCapacity = adjTrio.resCapacity;

                if (visited.contains(adjNode.name) || adjResCapacity <= 0){
                    if (adjResCapacity<0){
                        System.out.println("PRBOLEEMM");
                    }
                    continue;
                }

                // if KL is popped, back track with parent list and add new paths to them
                if (adjNode.name.equals(KL)){
                    currBottleneck = Math.min(minBottleneck, adjResCapacity);

                    parentTrios.put(adjNode.name, adjTrio);
                    parentNodes.put(adjNode.name, curr);
                    // backtrack the parents list
                    Node next = adjNode;

                    while (parentTrios.get(next.name) != null){
                        Trio parentTrio = parentTrios.get(next.name);
                        // reduce the weight in original path
                        parentTrio.resCapacity -= currBottleneck;
                        if (parentTrio.resCapacity < 0){
                            int a= 1;
                        }
                        Node parentNode = parentNodes.get(next.name);
                        // add the reverse path
                        next.adj_list.push(new Trio(parentNode, false, currBottleneck));
                        next = parentNode;
                    }

                    return true;
                }
                // add it to the visited
                visited.add(adjNode.name);
                queue.add(new Trio(adjNode, true, Math.min(minBottleneck, adjResCapacity)));
                parentTrios.put(adjNode.name, adjTrio);
                parentNodes.put(adjNode.name, curr);

            }
        }

        // if kl is not pooped and queue is empty return false
        return false;
    }
    private static boolean DFSHelper(Node curr, Set<String> visited, int minDistance) {
        visited.add(curr.name);
        for (Trio trio : curr.adj_list) {
            Node adj = trio.node;
            Integer resCapacity = trio.resCapacity;

            if (visited.contains(adj.name) || resCapacity <= 0){
                continue;
            }

            if (adj.name.equals(KL)){
                currBottleneck = Math.min(minDistance, resCapacity);
            }

            if (adj.name.equals(KL) || DFSHelper(adj, visited, Math.min(minDistance, resCapacity))){
                // update the residual graph
                // decrease from the path
                trio.resCapacity -= currBottleneck;
                // add a new edge
                adj.adj_list.push(new Trio(curr, false, currBottleneck));
                return true;
            }

        }

        return false;
    }

    private static TreeSet<String> minCut(Node source){
        TreeSet<String> cuts = new TreeSet<>();
        HashSet<String> reachable = new HashSet<>();
        
        // find which nodes are reachable from the source, ignoring the 0 weight paths
        // use the residual graph
        Queue<Node> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()){
            Node curr = queue.poll();
            if (reachable.contains(curr.name)){
                continue;
            }

            reachable.add(curr.name);
            for (Trio trio : curr.adj_list) {
                Node adjNode = trio.node;
                boolean original = trio.original;
                Integer adjDistance = trio.resCapacity;
                if (reachable.contains(adjNode.name) || adjDistance <= 0 ){
                    continue;
                }
                queue.add(adjNode);
            }
        }


        // now find all edges with one reachable vertex and one unreachable vertex
        // again start bfs but this time regard the 0 weight paths
        // use the original graph
        HashSet<String> visited = new HashSet<>();
        queue.add(source);
        while (!queue.isEmpty()){
            Node curr = queue.poll();

            if (visited.contains(curr.name)){
                continue;
            }
            visited.add(curr.name);

            for (Trio trio : curr.adj_list) {
                Node adjNode = trio.node;
                boolean original = trio.original;
                if (visited.contains(adjNode.name) || !original){
                    continue;
                }
                
                if (reachable.contains(adjNode.name)){
                    queue.add(adjNode);
                }
                else { // it is an unreachable node
                    cuts.add(curr.name+" "+adjNode.name);
                }
            }
        }
        
        return cuts;
    }
}