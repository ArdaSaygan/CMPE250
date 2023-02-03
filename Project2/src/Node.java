public class Node {
    private String ip;
    private Node right;
    private Node left;

    public Node(String ip){
        this.ip = ip;
        this.right = null;
        this.left = null;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public String getIP(){
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }





    
}
