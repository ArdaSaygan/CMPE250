import java.util.ArrayList;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

public class AVL {
    AVLNode root;
    ArrayList<String> logs;
    public AVL(String ip){
        root = new AVLNode(ip, 0);
        logs = new ArrayList<>();
    }

    // add
    public void add(String ip){
        root = add(ip, root);
    }
    private AVLNode add(String ip, AVLNode curr){
        if (root == null){
            logs.add("NONE" + ": New node being added with IP:" + ip + '\n');
            return new AVLNode(ip, 0);
        }
        
        if (curr == null){
            return new AVLNode(ip, 0);
        }


        logs.add(curr.getIp() + ": New node being added with IP:" + ip + '\n');
        // ip is greater
        if(curr.getIp().compareTo(ip) < 0){
            curr.setRightAvlNode( this.add(ip, curr.getRightAvlNode()));
            curr = this.balance(curr);
        }
        // ip is less
        else if(curr.getIp().compareTo(ip) > 0){
            curr.setLeftAvlNode( this.add(ip, curr.getLeftAvlNode()));
            curr = this.balance(curr);
        }
        else {
            System.out.println("The value you are trying to add is already in the tree");
        }
        return curr;

    }

    // balancing
    private AVLNode balance(AVLNode curr) {
        // check if it's balanced

        // right side is imbalanced
        if (curr.get_height_diff() > 1){
            if(curr.getRightAvlNode().get_height_diff() >= 0){
                // left rotation in curr
                logs.add("Rebalancing: left rotation\n");
                curr = this.leftRotation(curr);
            }
            else{
                // right left in curr
                logs.add("Rebalancing: right-left rotation\n");
                curr = this.rightLeftRotation(curr);
            }
        }
        // left side is imbalanced
        else if (curr.get_height_diff() < -1){
            if(curr.getLeftAvlNode().get_height_diff() <= 0){
                // right rotation in curr
                logs.add("Rebalancing: right rotation\n");
                curr = this.rightRotation(curr);
            }
            else {
                // left right rotation in curr
                logs.add("Rebalancing: left-right rotation\n");
                curr = this.leftRightRotation(curr);
            }
        }
        // what if it has no children or only one children
        curr.update_height();
        return curr;
    }

    private AVLNode leftRotation(AVLNode curr){
        boolean changeRoot = false;
        if (curr.equals(root)){
            changeRoot = true;
        }

        AVLNode right = curr.getRightAvlNode();
        AVLNode rightleft = right.getLeftAvlNode();
        right.setLeftAvlNode(curr);
        curr.setRightAvlNode(rightleft);

        right.update_height();
        curr.update_height();

        root = changeRoot ? right : root;
        return right;
    }

    private AVLNode rightRotation(AVLNode curr){
        boolean changeRoot = false;
        if (curr.equals(root)){
            changeRoot = true;
        }

        AVLNode left = curr.getLeftAvlNode();
        AVLNode leftright = left.getRightAvlNode();
        left.setRightAvlNode(curr);
        curr.setLeftAvlNode(leftright);

        left.update_height();
        curr.update_height();
        root = changeRoot ? left : root;
        return left;
    }

    private AVLNode rightLeftRotation(AVLNode curr){
        curr.setRightAvlNode( this.rightRotation(curr.getRightAvlNode()) );
        return this.leftRotation(curr);
    }

    private AVLNode leftRightRotation(AVLNode curr){
        curr.setLeftAvlNode( this.leftRotation(curr.getLeftAvlNode()));
        return this.rightRotation(curr);
    }

    // remove
    public void remove(String ip){
        root = remove(ip, root, null, true);
    }
    private AVLNode remove(String ip, AVLNode curr, AVLNode parentNode, boolean logbool){        

        if (curr == null){
            return curr;
        }

        boolean rootDel = false;
        if (root.getIp().equals(ip)){
            return curr;
            // rootDel = true;
        }

        // ip is greater
        if (curr.getIp().compareTo(ip) < 0){
            curr.setRightAvlNode(this.remove(ip, curr.getRightAvlNode(), curr, logbool));
            return balance(curr);
        }
        else if (curr.getIp().compareTo(ip) > 0){
            curr.setLeftAvlNode(this.remove(ip, curr.getLeftAvlNode(), curr, logbool));
            return balance(curr);
        }
        else{

            if (curr.getRightAvlNode() != null && curr.getLeftAvlNode() != null){
                String smallestIp = findSmallestNode(curr.getRightAvlNode()).getIp();

                if(rootDel){
                    logs.add("NONE" + ": Non Leaf Node Deleted; removed: " + curr.getIp() + " replaced: " + smallestIp+ '\n');
                }
                else{
                    logs.add(parentNode.getIp()+ ": Non Leaf Node Deleted; removed: " + curr.getIp() + " replaced: " + smallestIp+ '\n');
                }
                
                curr.setIp( smallestIp );
                AVLNode newRc = remove(smallestIp, curr.getRightAvlNode(), curr, false);
                // === added this
                curr.setRightAvlNode(newRc);

                return balance(curr);
            }
            else if (curr.getRightAvlNode() == null && curr.getLeftAvlNode() == null){

                if (logbool){
                    if (rootDel){
                        logs.add("NONE" + ": Leaf Node Deleted: " + curr.getIp()+ '\n') ;
                    }
                    else{
                        logs.add(parentNode.getIp() + ": Leaf Node Deleted: " + curr.getIp()+ '\n') ;
                    }   
                }
                    
                return null;
            }
            else{
                if (logbool){
                    if (rootDel){
                        logs.add("NONE" + ": Node with single child Deleted: " + curr.getIp()+ '\n');
                    }
                    else{
                        logs.add(parentNode.getIp() + ": Node with single child Deleted: " + curr.getIp()+ '\n');
                    }
                }

                return (curr.getRightAvlNode() == null ? curr.getLeftAvlNode():curr.getRightAvlNode());
            }

        }
    }

    // send messeage . same code with bst
    public void sendMessage(String senderIp, String receiverIp) {
        if (root == null || senderIp.equals(receiverIp)){
            return;
        }

        logs.add(senderIp+": Sending message to: "+receiverIp+"\n");

        // first find the common ancestor
        AVLNode curr = root;

        // if they are at the same side of the curr node and  none of them equals to the curr
        while (curr.getIp().compareTo(receiverIp) * curr.getIp().compareTo(senderIp) > 0) {
            // find which side they are

            // receiverIp is greater 
            if (curr.getIp().compareTo(receiverIp) < 0){
                // they are at the right side
                curr = curr.getRightAvlNode();
                continue;
            }
            curr = curr.getLeftAvlNode();
            continue;
        }


        // find which subtrees senders and receivers are
        // print the nodes to sender 
      
       if (curr.getIp().compareTo(senderIp) == 0){
           // do nothing you will print the root anyways
        }
        // sender is greater
        else if (curr.getIp().compareTo(senderIp) < 0){
            helperSenderPrinter(curr.getRightAvlNode(), senderIp, receiverIp);
            if (curr.getIp().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add( curr.getIp() + ": Transmission from: " + curr.getRightAvlNode().getIp()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
        }
        else if (curr.getIp().compareTo(senderIp) > 0){
            helperSenderPrinter(curr.getLeftAvlNode(), senderIp, receiverIp);
            if (curr.getIp().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add( curr.getIp() + ": Transmission from: " + curr.getLeftAvlNode().getIp()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
        }
        

        // print the nodes to receiver 
        if (curr.getIp().compareTo(receiverIp) == 0){
            // dont do anything you are done 
            return;
        }
        // receiver is greater
        else if (curr.getIp().compareTo(receiverIp) < 0){
            if (curr.getRightAvlNode().getIp().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add(curr.getRightAvlNode().getIp() + ": Transmission from: " + curr.getIp()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
            helperReceiverPrinter(curr.getRightAvlNode(), receiverIp, senderIp);
        }
        else if (curr.getIp().compareTo(receiverIp) > 0){
            if (curr.getLeftAvlNode().getIp().equals(receiverIp)){
                logs.add(receiverIp + ": Received message from: " + senderIp + '\n');
                return;
            }
            logs.add(curr.getLeftAvlNode().getIp() + ": Transmission from: " + curr.getIp()  + " receiver: " + receiverIp + " sender:" + senderIp + '\n');
            helperReceiverPrinter(curr.getLeftAvlNode(), receiverIp, senderIp);
        }

    }
   

    // helper functions . same code with bst
    private void helperSenderPrinter(AVLNode curr, String senderip, String receiverip){
        // senderip greater
        if (curr.getIp().compareTo(senderip) < 0){
            helperSenderPrinter(curr.getRightAvlNode(), senderip, receiverip);
            logs.add( curr.getIp() + ": Transmission from: " + curr.getRightAvlNode().getIp()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
        }
        else if (curr.getIp().compareTo(senderip) > 0){
            helperSenderPrinter(curr.getLeftAvlNode(), senderip, receiverip);
            logs.add( curr.getIp() + ": Transmission from: " +  curr.getLeftAvlNode().getIp()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
        }
        else{
            return;
        }
        
    }

    private void helperReceiverPrinter(AVLNode curr, String receiverip, String senderip){
           
        // receiverip greater
        if (curr.getIp().compareTo(receiverip) < 0 ){
            if (curr.getRightAvlNode().getIp().equals(receiverip)){
                logs.add(receiverip + ": Received message from: " + senderip + '\n');
                return;
            }
            logs.add(curr.getRightAvlNode().getIp() + ": Transmission from: " + curr.getIp()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
            helperReceiverPrinter(curr.getRightAvlNode(), receiverip, senderip);
        }
        else if (curr.getIp().compareTo(receiverip) > 0 ){
            if (curr.getLeftAvlNode().getIp().equals(receiverip)){
                logs.add(receiverip + ": Received message from: " + senderip + '\n');
                return;
            }
            logs.add(curr.getLeftAvlNode().getIp() + ": Transmission from: " + curr.getIp()  + " receiver: " + receiverip + " sender:" + senderip + '\n');
            helperReceiverPrinter(curr.getLeftAvlNode(), receiverip, senderip);
        }
        else{
            return;
        }
    }


    //find the smallest node
    private static AVLNode findSmallestNode(AVLNode curr) {
        while (curr.getLeftAvlNode() != null){
            curr = curr.getLeftAvlNode();
        }
        return curr;
    }

    // return the log
    public ArrayList<String> getLogs() {
        return logs;
    }
}
