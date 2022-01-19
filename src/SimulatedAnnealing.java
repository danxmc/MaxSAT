import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SimulatedAnnealing {
    public Timer timer;
    public MaxSATReader maxSATReader;
    private static double ALPHA = 0.95;
    private static int MAX_ITER = 3;
    private static final double INITIAL_TEMPERATURE = 10000;
    private static final double FINAL_TEMPERATURE = 0.00001;
    private List<Integer> bestFoundSol = new ArrayList<>();

    // Deserialized instances from file
    public MaxSATInstance maxSATInstance;
    public ArrayList<KnapsackOptimumSolutionInstance> knapsackOptimumSolutionInstances;

    public SimulatedAnnealing(String fileUri) {
        timer = new Timer();

        maxSATReader = new MaxSATReader(fileUri);
        maxSATInstance = maxSATReader.deserializeMaxSATInstance();
        System.out.println(maxSATInstance.print());
        // kReader.deserializeKnapsackOptimizationInstances(knapsackOptimizationInstances);
        // maxSATReader.setFileUri(MaxSATUtils.getSolUri(fileUri));
        // maxSATReader.deserializeKnapsackOptimumSolutionInstance(knapsackOptimumSolutionInstances);
    }

    public void getSolution() {
        bestFoundSol = new ArrayList<Integer>(Collections.nCopies(maxSATInstance.getN(), 0));
        // bestFoundSol.add(1);
        // bestFoundSol.add(0);
        // bestFoundSol.add(0);
        // bestFoundSol.add(1);

        timer.start();
        // System.out.println(bestFoundSol +" "+ maxSATInstance);
        List<Integer> solution = solve(maxSATInstance.getN(), maxSATInstance.getF(), maxSATInstance.getWeights());
        timer.end();
        System.out.println("finalAns " + solution);
    }

    private List<Integer> solve(int n, List<List<Integer>> clauses, List<Integer> weights) {

        double t = INITIAL_TEMPERATURE;
        List<Integer> state = MaxSATUtils.generateRandomValidSolution(n, clauses);

        int maxIter = MAX_ITER * n;
        while (t > FINAL_TEMPERATURE) {
            int innerIter = 0;
            while (innerIter <= maxIter) {
                innerIter++;
                List<Integer> newState = randomNeighbor(state);

                if (MaxSATUtils.checkSAT(clauses, newState)) {
                    System.out.println("accepted " + newState);
                    int neighborCost = MaxSATUtils.getSolutionTotalCost(weights, newState);
                    int stateCost = MaxSATUtils.getSolutionTotalCost(weights, state);
                    int deltaC = neighborCost - stateCost;

                    // Accept solution if better
                    if (deltaC > 0) {
                        state = newState;
                    } else if (accept(deltaC, t)) { // Accept probabilistically even if solution is worse
                        state = newState;
                    }

                    int bestFoundCost = MaxSATUtils.getSolutionTotalCost(weights, bestFoundSol);
                    if (stateCost > bestFoundCost) {
                        bestFoundSol = new ArrayList<>(state);
                    }
                }

            }

            t = cool(t);
        }
        return bestFoundSol;
    }

    private List<Integer> randomNeighbor(List<Integer> state) {
        int n = state.size();
        List<Integer> result = new ArrayList<>(state);
        // Get random num between 0 to n - 1
        int i = ThreadLocalRandom.current().nextInt(0, n);

        if (result.get(i) == 0) {
            result.set(i, 1);
        } else if (result.get(i) == 1) {
            result.set(i, 0);
        }

        return result;
    }

    private double cool(double t) {
        // Geometric cooling
        return t *= ALPHA;
    }

    // When deltaC increases, e^(-deltaC/t) decreases
    // the higher the cost difference, the lower the acceptance probability
    // When t decreases, e^(-deltaC/t) decreases
    // the lower the temperature, the lower the acceptance probability
    private boolean accept(int deltaC, double t) {
        double acceptP = Math.pow(Math.E, 1 * deltaC / t);
        double p = Math.random();
        return p < acceptP;
    }
}
