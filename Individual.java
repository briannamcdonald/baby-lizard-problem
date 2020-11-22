
public class Individual {
    Integer[][] gene;
    int fitness;

    public Individual(int boardSize) {
        gene = new Integer[boardSize][boardSize];
        fitness = Integer.MAX_VALUE;
    }
}