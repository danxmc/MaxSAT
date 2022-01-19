public class Main {

    public static void main(String[] args) {
        // Instance routes
        // String fileURI = "src/MaxSATInstances/4-6-instance.dat";
        // String fileURI = "src/MaxSATInstances/uf20-01.cnf";
        String fileURI = "src/MaxSATInstances/uf50-06.cnf";
        // String fileURI = "src/MaxSATInstances/ai/hoos/Shortcuts/UF75.325.100/uf75-01.cnf";

        // HW 5
        // System.out.println("Ti Tf Alpha	Max.Iter. Rel.E. Time(ms) TotalCost");
        getBySimulatedAnnealing(fileURI);
        // for (int i = 0; i < 100; i++) {
        // }
    }

    private static void getBySimulatedAnnealing(String fileUri) {
        SimulatedAnnealing simulatedAnnealingSol = new SimulatedAnnealing(fileUri);
        simulatedAnnealingSol.getSolution();
    }
}