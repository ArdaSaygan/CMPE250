import java.util.LinkedList;

public class Node {
    String name;

    LinkedList<Trio> adj_list = new LinkedList<>(); // adjnode , weight of the edge = limit of the flow

    public Node(String name){
        this.name = name;
    }
}
