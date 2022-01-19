import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaxSATInstance {
    private int n; // number of variables
    private int c; // number of clauses
    private List<Integer> weights; // weights
    private List<List<Integer>> F; // clauses array

    private float time;

    private int solutionCost;
    private double relativeError;
    private double performanceGuarantee;

    private List<Integer> solution;

    public MaxSATInstance(int n, int c) {
        this.n = n;
        this.c = c;
        this.weights = new ArrayList<>(this.n);
        this.F = new ArrayList<>(this.c);

        this.solutionCost = 0;
        this.relativeError = 0;
        this.performanceGuarantee = 1;
        this.time = 0;
        this.solution = new ArrayList<>();
    }

    public MaxSATInstance(String n, String c) {
        this.n = Integer.parseInt(n);
        this.c = Integer.parseInt(c);
        this.weights = new ArrayList<>(this.n);
        this.F = new ArrayList<>(this.c);

        this.solutionCost = 0;
        this.relativeError = 0;
        this.performanceGuarantee = 1;
        this.time = 0;
        this.solution = new ArrayList<>();
    }

    public int getN() {
        return n;
    }

    public int getC() {
        return c;
    }

    public List<Integer> getWeights() {
        return weights;
    }

    public List<List<Integer>> getF() {
        return F;
    }

    public float getTime() {
        return time;
    }

    public int getSolutionCost() {
        return solutionCost;
    }

    public double getRelativeError() {
        return relativeError;
    }

    public double getPerformanceGuarantee() {
        return performanceGuarantee;
    }

    public List<Integer> getSolution() {
        return solution;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setC(Integer c) {
        this.c = c;
    }

    public void setWeights(List<Integer> weights) {
        this.weights = weights;
    }

    public void setF(List<List<Integer>> clausesList) {
        this.F = clausesList;
    }

    public void setTime(long time) {
        float timeMs = time / 1000000.0f;
        this.time = timeMs;
    }

    public void setSolutionCost(int solutionCost) {
        this.solutionCost = solutionCost;
    }

    public void setRelativeError(float relativeError) {
        this.relativeError = relativeError;
    }

    public void setPerformanceGuarantee(float performanceGuarantee) {
        this.performanceGuarantee = performanceGuarantee;
    }

    public void setSolution(List<Integer> solution) {
        this.solution = solution;
        // this.calculateSolutionCost();
    }

    public String computationInfoToString() {
        return this.relativeError + " " + this.performanceGuarantee + " " + this.time;
    }

    public String print() {
        return this.n + " " + this.c + " " + this.weights + " " + this.F;
    }

    public String solutionInfoToString() {
        String solutionString = this.solution.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(" ", "", ""));
        return this.n + " " + this.solutionCost + " " + solutionString;
    }

    // function to calculate the cost of a knapsack solution
    // public int calculateSolutionCost() {
    // this.solutionCost = MaxSATUtils.getSolutionWeight(this.n, this.clausesList,
    // this.solution);
    // return this.solutionCost;
    // }

    public double calcRelativeErrorMaximization(int optC, int aprC) {
        if (optC > 0) {
            this.relativeError = (double) (optC - aprC) / optC;
        } else {
            this.relativeError = 1 - (double) 1 / this.performanceGuarantee;
        }
        return this.relativeError;
    }

    public double calcRelativeErrorMinimization(int optC, int aprC) {
        if (aprC > 0) {
            this.relativeError = (double) (aprC - optC) / aprC;
        }
        return this.relativeError;
    }

    public double calcPerformanceGuaranteeMaximization(int optC, int aprC) {
        if (aprC > 0) {
            this.performanceGuarantee = (double) optC / aprC;
        } else {
            this.performanceGuarantee = (double) 1 / (1 - this.relativeError);
        }
        return this.performanceGuarantee;
    }

    public double calcPerformanceGuaranteeMinimization(int optC, int aprC) {
        if (optC > 0) {
            this.performanceGuarantee = (double) aprC / optC;
        }
        return this.performanceGuarantee;
    }

    public String getCNFAnswer() {
        List<Integer> solutionLiterals = MaxSATUtils.getSolvedAssignedLiterals(this.solution);
        String solutionLiteralsString = solutionLiterals.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(" ", "", ""));
        return "cnf " + this.solutionCost + " " + solutionLiteralsString + " 0";
    }

    @Override
    public String toString() {
        return this.n + " " + this.c + " " + this.F + " " + this.time + " " + this.relativeError + " "
                + this.performanceGuarantee + " " + this.solution;
    }
}
