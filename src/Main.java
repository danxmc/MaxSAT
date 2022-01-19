public class Main {

    public static void main(String[] args) {
        // Instance routes
        String fileURI = "src/MaxSATInstances/4-6-instance.dat";

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