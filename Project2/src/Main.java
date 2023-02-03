import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;


//// PROBLEMS
/*
 * If a measseage from a neighbor is sent loggung doesnt work correctly
 */


public class Main {
    public static void main(String[] args) throws Exception {

        String out_bst_path = (String.format("%s_bst.txt", args[1]));
        String out_avl_path = (String.format("%s_avl.txt", args[1]));
        System.out.println(out_bst_path);
        run(args[0], out_bst_path, out_avl_path, out_bst_path, out_avl_path);

        /* 
        for (int i = 1; i <= 100; i++) {
            
            if (i!=2){
                //continue;
            }
            System.out.println("->"+i);
            run("inputs3/input"+i, "myputs3/input"+i+"_BST.txt", "myputs3/input"+i+"_AVL.txt", "outputumB.txt", "outputumAVL.txt");
        }    
        */
        // run("customin", "outputumB.txt",  "outputumAVL.txt" ,"outputumB.txt", "outputumAVL.txt");
    }

    public static void run(String inputP, String ToutputBP, String ToutputAP ,String outputBP, String outputAP) throws IOException {
        File inputFile = new File(inputP);
        File targetoutB = new File(ToutputBP);
        File targetoutA = new File(ToutputAP);
        File myoutB = new File(outputBP);
        File myoutA = new File(outputAP);

        Scanner input = new Scanner(inputFile);
        // Scanner toutputBScanner = new Scanner(targetoutB);
        // Scanner toutputAScanner = new Scanner(targetoutA);

        FileWriter writerB = new FileWriter(myoutB);
        FileWriter writerA = new FileWriter(myoutA);

        String rootIp = input.nextLine();
        Binary bt = new Binary(rootIp);
        AVL avl = new AVL(rootIp);

        int satir = 0;
        while (input.hasNextLine()) {
            satir++;
            String[] line = input.nextLine().split(" ");
            switch (line[0]) {
                case "ADDNODE":

                    avl.add(line[1]);
                    bt.add(line[1]);
                    break;

                case "DELETE":
                    avl.remove(line[1]);
                    bt.remove(line[1]);
                    break;

                case "SEND":
                    avl.sendMessage(line[1], line[2]);
                    bt.sendMessage(line[1], line[2]);
                    break;

                default:
                    break;
            }
        }
        
        for (String line : bt.getLogs()) {
            writerB.write(line);
        }
        writerB.flush();
        for (String line : avl.getLogs()) {
            writerA.write(line);
        }
        writerA.flush();  

        /* 
        Scanner myoutputBScanner = new Scanner(myoutB);
        Scanner myyoutputAScanner = new Scanner(myoutA);

        
        while (myoutputBScanner.hasNextLine() && toutputBScanner.hasNextLine()) {
            String T = toutputBScanner.nextLine();
            String mine = myoutputBScanner.nextLine();
            if (mine.equals(T)){
                continue;
            }
            System.out.println("B  > T " + T + "   > M " + mine );
            break;
        }
        if (myoutputBScanner.hasNextLine() || toutputBScanner.hasNextLine()){
            System.out.println("B > diff len" );
        } 

        while (myyoutputAScanner.hasNextLine() && toutputAScanner.hasNextLine()) {
            String T = toutputAScanner.nextLine();
            String mine = myyoutputAScanner.nextLine();
            if (mine.equals(T)){
                continue;
            }
            System.out.println("A  > T " + T + "   > M " + mine );
            break;
        }
        if (myyoutputAScanner.hasNextLine() || toutputAScanner.hasNextLine()){
            System.out.println("A > diff len");
        } */
    }
}
