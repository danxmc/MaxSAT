import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class MaxSATUtils {

    // Private constructor to prevent instantiation
    private MaxSATUtils() {
        throw new UnsupportedOperationException();
    }

    // public static methods

    // public static void calculateQualityMeasurements(MaxSATInstance
    // knapsackOptimizationInstance,
    // ArrayList<KnapsackOptimumSolutionInstance> knapsackOptimumSolutionInstances)
    // {
    // int calculatedSolCost = knapsackOptimizationInstance.calculateSolutionCost();
    // KnapsackOptimumSolutionInstance knapsackOptimumSolutionInstance =
    // findKnapsackOptimumSolutionInstance(
    // knapsackOptimumSolutionInstances, knapsackOptimizationInstance.getId());

    // if (knapsackOptimumSolutionInstance != null) {
    // knapsackOptimizationInstance.calcRelativeErrorMaximization(
    // knapsackOptimumSolutionInstance.getSolutionCost(), calculatedSolCost);
    // knapsackOptimizationInstance.calcPerformanceGuaranteeMaximization(
    // knapsackOptimumSolutionInstance.getSolutionCost(), calculatedSolCost);
    // }
    // }

    public static String getSolUri(String fileUri) {
        return fileUri.replace("_inst.dat", "_sol.dat");
    }

    public static List<Integer> generateLocalConstructiveSolution(int n, int M, ArrayList<Integer> W,
            ArrayList<Integer> C) {
        // List<Integer> solution = Heuristic.solve(n, M, W, C);
        // List<Integer> solution = Heuristic.solveExtended(n, M, W, C);
        // return solution;
        return null;
    }

    public static List<Integer> generateRandomValidSolution(int n, List<List<Integer>> clauses) {
        List<Integer> solution = new ArrayList<>();
        boolean isValidSol = false;
        while (!isValidSol) {
            solution = generateRandomSolution(n);
            if (MaxSATUtils.checkSAT(clauses, solution)) {
                isValidSol = true;
            }
        }
        return solution;
    }

    public static List<Integer> generateRandomSolution(int n) {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // Get random num between 0 - 1
            int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            solution.add(randomNum);
        }
        return solution;
    }

    public static int getSolutionTotalCost(List<Integer> W, List<Integer> solution) {
        int solutionWeight = 0;
        for (int i = 0; i < W.size(); i++) {
            int currentItemWeight = W.get(i);
            solutionWeight += currentItemWeight * solution.get(i);
        }
        return solutionWeight;
    }

    public static boolean intToBool(int val) {
        if (val <= 0) {
            return false;
        }
        return true;
    }

    public static boolean checkSAT(List<List<Integer>> clauses, List<Integer> state) {
        boolean isSAT = true;
        List<List<Boolean>> assignedClauses = new ArrayList<>();

        for (int i = 0; i < clauses.size(); i++) {
            List<Integer> currentClause = clauses.get(i);
            List<Boolean> assignedLiterals = new ArrayList<>();

            for (int j = 0; j < currentClause.size(); j++) {
                int currentLiteral = currentClause.get(j);
                int stateLiteral = state.get(Math.abs(currentLiteral) - 1);
                boolean assignedLiteral;

                if (currentLiteral > 0) {
                    assignedLiteral = MaxSATUtils.intToBool(stateLiteral);
                } else {
                    assignedLiteral = !MaxSATUtils.intToBool(stateLiteral);
                }
                assignedLiterals.add(assignedLiteral);
            }
            assignedClauses.add(assignedLiterals);
        }

        List<Boolean> isSATClauseList = new ArrayList<>();
        for (int i = 0; i < assignedClauses.size(); i++) {
            boolean isSATClause = false;
            List<Boolean> assignedClause = assignedClauses.get(i);

            for (int j = 0; j < assignedClause.size(); j++) {
                boolean assignedLiteral = assignedClause.get(j);
                if (assignedLiteral) {
                    isSATClause = true;
                    j = assignedClause.size();
                }
            }

            if (!isSATClause) {
                isSAT = false;
                i = assignedClauses.size();
            }
            isSATClauseList.add(isSATClause);
        }
        return isSAT;
    }
}