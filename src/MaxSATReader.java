import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaxSATReader {
    private String fileUri;

    public MaxSATReader(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public void getFileInfo() {
        File myObj = new File(this.fileUri);
        if (myObj.exists()) {
            System.out.println("File name: " + myObj.getName());
            System.out.println("Absolute path: " + myObj.getAbsolutePath());
            System.out.println("Writeable: " + myObj.canWrite());
            System.out.println("Readable " + myObj.canRead());
            System.out.println("File size in bytes " + myObj.length());
        } else {
            System.out.println("The file does not exist.");
        }
    }

    public void readFile() {
        try {
            File myObj = new File(this.fileUri);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void deserializeKnapsackOptimumSolutionInstance(
            ArrayList<KnapsackOptimumSolutionInstance> knapsackSolutions) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileUri));

            String currentLine;
            try {
                while ((currentLine = br.readLine()) != null) {
                    // Parse to Obj
                    String[] strArgs = currentLine.split(" ");

                    KnapsackOptimumSolutionInstance knapsackInstance = new KnapsackOptimumSolutionInstance(strArgs[0],
                            strArgs[1], strArgs[2]);
                    for (int j = 3; j < strArgs.length; j++) {
                        // Assign solution arrays
                        knapsackInstance.getSolution().add(Integer.parseInt(strArgs[j]));
                    }
                    knapsackSolutions.add(knapsackInstance);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MaxSATInstance deserializeMaxSATInstance() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileUri));
            String currentLine;

            int n = 0;
            int c = 0;
            List<Integer> W = new ArrayList<>();
            List<List<Integer>> clausesList = new ArrayList<>();
            try {
                while ((currentLine = br.readLine()) != null) {
                    // Parse to separated by space array
                    String[] strArgs = currentLine.trim().split("\\s+");
                    switch (strArgs[0]) {
                        case "w":
                            // Assign weights array
                            for (int i = 1; i < strArgs.length; i++) {
                                int currW = Integer.parseInt(strArgs[i]);
                                if (currW != 0) {
                                    W.add(currW);
                                }
                            }
                            break;

                        case "p":
                            // n variables and clauses information
                            n = Integer.parseInt(strArgs[2]);
                            c = Integer.parseInt(strArgs[3]);
                            break;
                        case "c":
                            // Instance comment
                            break;
                        case "%":
                            break;
                        case "0":
                            break;
                        case "":
                            break;
                        case default:
                            // Instance clause
                            List<Integer> clause = new ArrayList<>();
                            for (int i = 0; i < strArgs.length; i++) {
                                int currVar = Integer.parseInt(strArgs[i]);
                                if (currVar == 0) {
                                    i = strArgs.length;
                                } else {
                                    clause.add(currVar);
                                }
                            }
                            clausesList.add(clause);
                            break;
                    }

                }
                MaxSATInstance maxSAT = new MaxSATInstance(n, c);
                maxSAT.setF(clausesList);
                if (W.size() == 0) {
                    W = MaxSATUtils.generateRandomWeights(n);
                }
                maxSAT.setWeights(W);
                br.close();
                return maxSAT;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new MaxSATInstance(0, 0);
    }

}
