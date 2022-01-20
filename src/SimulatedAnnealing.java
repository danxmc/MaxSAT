import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulatedAnnealing {
    public Timer timer;
    public MaxSATReader maxSATReader;
    private List<Integer> bestFoundSol = new ArrayList<>();
    private static int MAX_TRIES = 5;
    private static final double MAX_TEMP = 100;
    private static final double MIN_TEMP = 0.01;

    private static int globalCounter = 0;

    // Deserialized instances from file
    public MaxSATInstance maxSATInstance;
    public ArrayList<KnapsackOptimumSolutionInstance> knapsackOptimumSolutionInstances;

    public SimulatedAnnealing(String fileUri) {
        timer = new Timer();

        maxSATReader = new MaxSATReader(fileUri);
        maxSATInstance = maxSATReader.deserializeMaxSATInstance();
        // System.out.println(maxSATInstance.print());
        // kReader.deserializeKnapsackOptimizationInstances(knapsackOptimizationInstances);
        // maxSATReader.setFileUri(MaxSATUtils.getSolUri(fileUri));
        // maxSATReader.deserializeKnapsackOptimumSolutionInstance(knapsackOptimumSolutionInstances);
    }

    public void getSolution() {
        bestFoundSol = new ArrayList<Integer>(Collections.nCopies(maxSATInstance.getN(), 0));

        // System.out.println(maxSATInstance.toString());

        timer.start();
        // System.out.println(bestFoundSol +" "+ maxSATInstance);
        List<Integer> solution = solve(maxSATInstance.getN(), maxSATInstance.getF(), maxSATInstance.getWeights());
        timer.end();
        maxSATInstance.setTime(timer.totalTime);
        maxSATInstance.setSolution(solution);

        // System.out.println("finalAns " + solution);
        // System.out.println(maxSATInstance.getCNFAnswer());
        System.out.println(maxSATInstance.computationInfoToString());
    }

    private List<Integer> solve(int n, List<List<Integer>> clauses, List<Integer> weights) {
        int i = 0;
        int tries = 0;

        temperatureLoop: while (true) {
            i++;
            int j = 0;
            List<Integer> T = MaxSATUtils.generateDPLLSolution(clauses);
            bestFoundSol = T;

            triesLoop: while (true) {
                double temperature = cool(i, j, n);

                if (temperature < MIN_TEMP) {
                    break triesLoop;
                }

                for (int k = 0; k < n; k++) {
                    // globalCounter++;
                    List<Integer> newT = randomNeighbor(T, k);

                    int neighborCost = MaxSATUtils.getSolutionTotalCost(weights, newT);
                    int stateCost = MaxSATUtils.getSolutionTotalCost(weights, T);
                    int deltaC = neighborCost - stateCost;

                    // Accept solution if better
                    if (deltaC > 0) {
                        T = newT;
                        // System.out.println(globalCounter + " " + neighborCost);
                        // System.out.println("Better " + newT + " T " + stateCost + " newT " +
                        // neighborCost);
                    } else if (accept(deltaC, temperature)) { // Accept probabilistically even if solution is worse
                        T = newT;
                        // System.out.println("Probs " + newT + " T " + stateCost + " newT " +
                        // neighborCost);
                        // System.out.println(globalCounter + " " + neighborCost);
                    } else {
                        // System.out.println(globalCounter + " " + stateCost);
                    }

                    int bestFoundCost = MaxSATUtils.getSolutionTotalCost(weights, bestFoundSol);
                    stateCost = MaxSATUtils.getSolutionTotalCost(weights, T);
                    if (stateCost > bestFoundCost && MaxSATUtils.checkSAT(clauses, T)) {
                        bestFoundSol = new ArrayList<>(T);
                    }
                }
                j++;
                tries++;
                if (tries == MAX_TRIES) {
                    break temperatureLoop;
                }
            }
        }
        return bestFoundSol;
    }

    private List<Integer> randomNeighbor(List<Integer> state, int i) {
        List<Integer> result = new ArrayList<>(state);

        if (result.get(i) == 0) {
            result.set(i, 1);
        } else if (result.get(i) == 1) {
            result.set(i, 0);
        }

        return result;
    }

    private double cool(int i, int j, int n) {
        return MAX_TEMP * Math.pow(Math.E, (-1 * j) / (1.0 / (i * n)));
    }

    private boolean accept(int deltaC, double t) {
        double acceptP = 1 / (1 + Math.pow(Math.E, -1 * (deltaC / t)));
        double p = Math.random();
        return p < acceptP;
    }
}
