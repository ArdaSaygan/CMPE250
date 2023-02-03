import java.lang.reflect.Constructor;

public class AVLNode {
    
    private String ip;
    private AVLNode leftAvlNode;
    private AVLNode rightAvlNode;
    public int height;


    public AVLNode(String ip, int height){
        this.ip = ip;
        this.height = height;
    }

    public AVLNode getLeftAvlNode() {
        return leftAvlNode;
    }
    public void setLeftAvlNode(AVLNode leftAvlNode) {
        this.leftAvlNode = leftAvlNode;
    }

    public AVLNode getRightAvlNode() {
        return rightAvlNode;
    }
    public void setRightAvlNode(AVLNode rightAvlNode) {
        this.rightAvlNode = rightAvlNode;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public int get_height_diff() {
        int heightR = (this.getRightAvlNode()==null) ? -1 : this.getRightAvlNode().height;
        int heightL = (this.getLeftAvlNode()==null) ? -1 : this.getLeftAvlNode().height;
        return heightR - heightL;
    }

    public int update_height() {
        int heightR = (this.getRightAvlNode()==null) ? -1 : this.getRightAvlNode().height;
        int heightL = (this.getLeftAvlNode()==null) ? -1 : this.getLeftAvlNode().height;
        this.height = Math.max(heightR, heightL) + 1;
        return height;
    }

}
