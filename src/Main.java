public class Main {

    public static void main(String[] args) {
        // Instance routes
        String fileURI = "src/MaxSATInstances/4-6-instance.dat";
        // String fileURI = "src/MaxSATInstances/uf20-01.cnf";
        // String fileURI = "src/MaxSATInstances/uf50-06.cnf";

        // HW 5
        getBySimulatedAnnealing(fileURI);
    }

    private static void getBySimulatedAnnealing(String fileUri) {
        SimulatedAnnealing simulatedAnnealingSol = new SimulatedAnnealing(fileUri);
        simulatedAnnealingSol.getSolution();
    }
}