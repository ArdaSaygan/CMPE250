import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Project1
 */
public class Project1 {

    public static void main(String[] args) throws IOException {
        
        
        // for test
        /* 
        for (int j = 1; j <= 50; j++) {
            run(String.format("src/inputs2/input%d.txt",j), "output.txt");
            Scanner sc1 = new Scanner(new File("output.txt"));
            Scanner sc2 = new Scanner(new File( String.format("src/outputs2/output%d.txt",j)));
            System.out.println("-->" + j);

            while (sc1.hasNextLine()) {
                if (!sc2.hasNextLine()){
                    System.out.println(String.format("output%d.txt",j) + " m");
                }

                if (!sc1.nextLine().equals(sc2.nextLine())){
                    System.out.println(String.format("output%d.txt",j) + " f");
                    break;
                }
            }
        }

        System.out.println("done"); */

        // for submission
        run(args[0],args[1]);
        
    }

    public static void run(String Inputpath, String Outputpath) throws IOException {

        Scanner sc = new Scanner( new File(Inputpath));
        FileWriter output = new FileWriter(new File(Outputpath));

        FactoryImpl factory = new FactoryImpl();
        
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(" ");

            switch (line[0]) {

                case "AF":
                    factory.addFirst( new Product( Integer.parseInt(line[1]), Integer.parseInt(line[2])));
                    break;
                
                case "AL":
                    factory.addLast( new Product( Integer.parseInt(line[1]), Integer.parseInt(line[2])));
                    break;
                    
                case "A":
                    try {
                        factory.add(  Integer.parseInt(line[1]),  new Product( Integer.parseInt(line[2]), Integer.parseInt(line[3])));
                    } catch (IndexOutOfBoundsException e) {
                        output.write("Index out of bounds.\n");
                    }
                    break;

                case "RF":
                    try {
                        output.write(factory.removeFirst().toString()+'\n');
                    } catch (NoSuchElementException e) {
                        output.write("Factory is empty.\n");
                    }
                    break;
                    
                case "RL":
                    try {
                        output.write(factory.removeLast().toString()+'\n');
                    } catch (NoSuchElementException e) {
                       output.write("Factory is empty.\n");
                    }
                    break;
                case "RI":
                    try {
                        output.write(factory.removeIndex(Integer.parseInt(line[1])).toString()+'\n');
                    } catch (IndexOutOfBoundsException e) {
                        output.write("Index out of bounds.\n");
                    }
                    break;

                case "RP":
                    try{
                        output.write(factory.removeProduct(Integer.parseInt(line[1])).toString()+'\n');
                    } catch(NoSuchElementException e){
                        output.write("Product not found.\n");
                    }
                    break;

                case "F":
                    try {
                        output.write(factory.find(Integer.parseInt(line[1])).toString()+'\n');
                    } catch (NoSuchElementException e) {
                        output.write("Product not found.\n");
                    }
                    break;

                case "G":
                    try {
                        output.write(factory.get(Integer.parseInt(line[1])).toString()+'\n');
                    } catch (IndexOutOfBoundsException e) {
                        output.write("Index out of bounds.\n");
                    }
                    break;
                
                case "U":
                    try {
                        output.write(factory.update(Integer.parseInt(line[1]), Integer.parseInt(line[2])).toString()+'\n');
                    } catch (NoSuchElementException e) {
                        output.write("Product not found.\n");
                    }
                    break;

                case "FD":
                    output.write(Integer.toString(factory.filterDuplicates()) + "\n");
                    break;
                
                case "R":
                    factory.reverse();
                    output.write(factory.toString()+'\n');
                    break;

                case "P":
                    output.write(factory.toString()+'\n');
                    break;

                default:
                    break;
            }

            output.flush();
        }


        sc.close();
        output.close();
        
    }
}