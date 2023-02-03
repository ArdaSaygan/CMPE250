import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;

public class Binary {
    private Node root;
    private ArrayList<String> logs;
    public Binary(String rootIp){
        this.root = new Node(rootIp);
        this.logs = new ArrayList<>();
    }

    //add node
    public void add(String ip){

        if (root == null){
            root = new Node(ip);
            // What should we log if the root is null and new node is being added.
            return;
        }

        Node curr = root;
        while (curr != null) {
            logs.add(curr.getIP() + ": New node being added with IP:" + ip + '\n');
            // a smaller value is added
            if (ip.compareTo(curr.getIP()) < 0) {
                if (curr.getLeft() == null){
                    curr.setLeft(new Node(ip));
                    break;
                }
                curr = curr.getLeft();
            }
            // a greater value is added
            else if (ip.compareTo(curr.getIP()) > 0) {
                if (curr.getRight() == null){
                    curr.setRight(new Node(ip));
                    break;
                }
                curr = curr.getRight();
            }
            else {
                
                System.out.println(ip + "  This value is already added to the binary tree!!!");
                break;
            }  
        }
        return;

    }

    //remove node
    public void remove(String ip) throws IOException{
        remove(ip, root, null, true);
    }
    private Node remove(String ip, Node curr, Node parentNode,Boolean logbool) throws IOException{
        if (curr == null){
            return curr;
        }

        // ip is greater than currIP
        if (curr.getIP().compareTo(ip) < 0){
            curr.setRight(remove(ip, curr.getRight(), curr, logbool));
            return curr;
        }
        // ip is less than currIP
        else if (curr.getIP().compareTo(ip) > 0){
            curr.setLeft(remove(ip, curr.getLeft(), curr,logbool));
            return curr;
        }

        else {
            
            // three cases
            // no children
            if (curr.getLeft() == null && curr.getRight() == null){
                if (logbool)
                    logs.add(parentNode.getIP() + ": Leaf Node Deleted: " + curr.getIP()+ '\n') ;
                return null;
            }
            // two children
            else if (curr.getLeft() != null && curr.getRight() != null){
                Node smallestRN = findSmallestNode(curr.getRight());
                logs.add(parentNode.getIP() + ": Non Leaf Node Deleted; removed: " + curr.getIP() + " replaced: " + smallestRN.getIP()+ '\n');
                curr.setRight( remove(smallestRN.getIP(), curr.getRight(), curr, false) );
                curr.setIp(smallestRN.getIP());
                return curr;
            }
            // one child
            else {
                if (logbool)
                    logs.add(parentNode.getIP() + ": Node with single child Deleted: " + curr.getIP()+ '\n');
                if(curr.getLeft() == null){
                    curr = curr.getRight();
                }
                else{
                    curr = curr.getLeft();
                }
                return curr;
            }
        }

        

    }


    //send message
    public void sendMessage(String senderIp, String receiverIp) {
        logs.add(senderIp+": Sending message to: "+receiverIp+"\n");

        // first find the common ancestor
        Node curr = root;

        // if they are at the same side of the curr node and  none of them equals to the curr
        while (curr.getIP().compareTo(receiverIp) * curr.getIP().compareTo(senderIp) > 0) {
            // find which side they are

            // receiverIp is greater 
            if (curr.getIP().compareTo(receiverIp) < 0){
                // they are at the right side
                curr = curr.getRight();
                continue;
            }
            curr = curr.getLeft();
            continue;
        }


        // find which subtrees senders and receivers are
        // print the nodes to sender 
      
       if (curr.getIP().compareTo(senderIp) == 0){
           // do nothing you will print the root anyways
        }
        // sender is greater
        else if (curr.getIP().compareTo(senderIp) < 0){
            helperSenderPrinter(curr.getRight(), senderIp, receiverIp);
            if (curr.getIP().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add( curr.getIP() + ": Transmission from: " + curr.getRight().getIP()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
        }
        else if (curr.getIP().compareTo(senderIp) > 0){
            helperSenderPrinter(curr.getLeft(), senderIp, receiverIp);
            if (curr.getIP().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add( curr.getIP() + ": Transmission from: " + curr.getLeft().getIP()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
        }
        

        // print the nodes to receiver 
        if (curr.getIP().compareTo(receiverIp) == 0){
            // dont do anything you are done 
            return;
        }
        // receiver is greater
        else if (curr.getIP().compareTo(receiverIp) < 0){
            if (curr.getRight().getIP().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add(curr.getRight().getIP() + ": Transmission from: " + curr.getIP()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
            helperReceiverPrinter(curr.getRight(), receiverIp, senderIp);
        }
        else if (curr.getIP().compareTo(receiverIp) > 0){
            if (curr.getLeft().getIP().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add(curr.getLeft().getIP() + ": Transmission from: " + curr.getIP()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
            helperReceiverPrinter(curr.getLeft(), receiverIp, senderIp);
        }

    }
    //return the log
    public ArrayList<String> getLogs() {
        return logs;
    }


    //print the tree

    //find the smallest node
    private static Node findSmallestNode(Node curr) {
        while (curr.getLeft() != null){
            curr = curr.getLeft();
        }
        return curr;
    }


    private void helperSenderPrinter(Node curr, String senderip, String receiverip){
        // senderip greater
        if (curr.getIP().compareTo(senderip) < 0){
            helperSenderPrinter(curr.getRight(), senderip, receiverip);
            logs.add( curr.getIP() + ": Transmission from: " + curr.getRight().getIP()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
        }
        else if (curr.getIP().compareTo(senderip) > 0){
            helperSenderPrinter(curr.getLeft(), senderip, receiverip);
            logs.add( curr.getIP() + ": Transmission from: " +  curr.getLeft().getIP()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
        }
        else{
            return;
        }
        
    }

    private void helperReceiverPrinter(Node curr, String receiverip, String senderip){
           
        // receiverip greater
        if (curr.getIP().compareTo(receiverip) < 0 ){
            if (curr.getRight().getIP().equals(receiverip)){
                logs.add(receiverip + ": Received message from: " + senderip + '\n');
                return;
            }
            logs.add(curr.getRight().getIP() + ": Transmission from: " + curr.getIP()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
            helperReceiverPrinter(curr.getRight(), receiverip, senderip);
        }
        else if (curr.getIP().compareTo(receiverip) > 0 ){
            if (curr.getLeft().getIP().equals(receiverip)){
                logs.add(receiverip + ": Received message from: " + senderip + '\n');
                return;
            }
            logs.add(curr.getLeft().getIP() + ": Transmission from: " + curr.getIP()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
            helperReceiverPrinter(curr.getLeft(), receiverip, senderip);
        }
        else{
            return;
        }
    }
}
