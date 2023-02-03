import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SpeedRunnerV2 {
    private final String dirName = "newCases/inputs2/"; // this is the directory of the inputs
    private final String realName = "newCases/outputs2/"; // this is the directory of the outputs that you want to compare
    private final String outName = "myputs/"; // this is the directory that you want to store your outputs
    // PLEASE CHECK THE VARIABLE NAMES ABOVE AND EDIT IF NECESSARY
    // ALL THESE DIRECTORIES MUST EXIST! IF YOU DON'T HAVE THIS DIRECTORIES PLEASE MAKE THEM
    // IF YOU GET A FILENOTFOUNDEXCEPTION ABOUT NOT EXISTING FILE TRY DELETING .DS_Store FILE
    // BELOW THIS LINE DO NOT CHANGE ANYTHING EXCEPT THE LINES 18-19

    private int fileNumber;

    public static void main(String[] args) throws IOException {
        SpeedRunnerV2 speedRunner = new SpeedRunnerV2();

        speedRunner.run(); // you can toggle this to execute the inputs
        speedRunner.compare(); // you can toggle this to compare yourputs (outName) with some outputs (realName)
    }

    public SpeedRunnerV2() {
        File inDir = new File(dirName);
        File realDir = new File(realName);
        File[] files = inDir.listFiles();
        File[] outFiles = realDir.listFiles();
        fileNumber = files != null ? files.length : 0;
        if (files != null && files[0].toString().equals(dirName.replace("/", "\\") + ".DS_Store")){
            fileNumber--;
        }
        int outNumber = outFiles != null ? outFiles.length : 0;
        if (outNumber - fileNumber > 1 || outNumber - fileNumber < -1){
            System.out.println("Warning: The numbers of inputs and outputs are not same!! Some inputs might not be compared");
            System.out.println("In this case you will have a FileNotFoundException so you must check your directories again");
        }
        System.out.println(fileNumber);
    }

    private void run() throws IOException {
        for (int i = 0; i < fileNumber; i++) {
            Project3.main(new String[]{dirName + "case" + i + ".in", outName + "case" + i + ".out"});
            System.out.println("Case" + i + " is executed");
        }
    }

    private void compare() throws IOException {
        for (int i = 0; i < fileNumber; i++) {
            File realOutput = new File(realName + "case" + i + ".out");
            File myOutput = new File(outName + "case" + i + ".out");

            int line = filesCompareByLine(realOutput, myOutput);
            String fileStr = "Case" + i;
            if (line == -1){
                System.out.println(fileStr + " : True" );
            }else {
                System.out.println(fileStr + " : False. Wrong line is " + line);
            }
        }
    }

    private static int filesCompareByLine(File path1, File path2) throws IOException {
        BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(path1), StandardCharsets.UTF_8));
        BufferedReader bf2 = new BufferedReader(new InputStreamReader(new FileInputStream(path2), StandardCharsets.UTF_8));
        HashSet<String> lines = new HashSet<>();

        int lineNumber = 1;
        int trueLine = 0;
        String line1 = "", line2 = "";
        while ((line1 = bf1.readLine()) != null) {
            lines.add(line1);
            trueLine++;
        }
        while ((line2 = bf2.readLine()) != null) {
            String[] lineElems = line2.split(" ");
            TreeSet<String> airports = new TreeSet<>(Comparator.comparingInt(o -> Integer.parseInt(o.substring(3))));
            airports.addAll(Arrays.asList(lineElems).subList(2, lineElems.length));
            line2 = lineElems[0] + " " + lineElems[1];
            for (String elem : airports) {
                line2 = line2.concat(" " + elem);
            }
            if (lines.contains(line2)){
                lineNumber++;
            }else {
                return lineNumber;
            }
        }
        if (lineNumber != 1 && lineNumber-1 == trueLine)
            return -1;
        return lineNumber;
    }
}
