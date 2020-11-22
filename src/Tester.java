
public class Tester {
    private static int boardSize = 6;

    public static void main(String[] args) {

        System.out.println("Testing createRandomIndividual");
        Individual rand1 = BabyLizardProblem.createRandomIndividual();
        Individual rand2 = BabyLizardProblem.createRandomIndividual();
        Individual rand3 = BabyLizardProblem.createRandomIndividual();
        System.out.println("Random individual 1: ");
        printGene(rand1);
        System.out.println("Random individual 2: ");
        printGene(rand2);
        System.out.println("Random individual 3: ");
        printGene(rand3);

        System.out.println();
        System.out.println("Test Fitness Function");
        Individual fitness1 = new Individual(6);
        Integer[] fitness11 = new Integer[] { 1, 1, 1, 0, 0, 1 };
        Integer[] fitness12 = new Integer[] { 0, 2, 0, 0, 0, 0 };
        Integer[] fitness13 = new Integer[] { 0, 0, 0, 0, 0, 0 };
        Integer[] fitness14 = new Integer[] { 0, 0, 0, 2, 0, 0 };
        Integer[] fitness15 = new Integer[] { 0, 0, 0, 0, 1, 0 };
        Integer[] fitness16 = new Integer[] { 0, 0, 2, 1, 0, 0 };
        fitness1.gene[0] = fitness11;
        fitness1.gene[1] = fitness12;
        fitness1.gene[2] = fitness13;
        fitness1.gene[3] = fitness14;
        fitness1.gene[4] = fitness15;
        fitness1.gene[5] = fitness16;
        System.out.println("Individual being evaluted:");
        printGene(fitness1);
        System.out.println("Expected fitness: 7");
        System.out.println("Actual Fitness: " + BabyLizardProblem.fitnessFunction(fitness1.gene));

        System.out.println();
        System.out.println("Test Fitness Function");
        Individual fitness2 = new Individual(6);
        Integer[] fitness21 = new Integer[] { 0, 0, 0, 0, 1, 0 };
        Integer[] fitness22 = new Integer[] { 0, 2, 0, 0, 0, 0 };
        Integer[] fitness23 = new Integer[] { 1, 0, 0, 0, 0, 0 };
        Integer[] fitness24 = new Integer[] { 0, 0, 0, 2, 0, 0 };
        Integer[] fitness25 = new Integer[] { 0, 1, 1, 0, 0, 0 };
        Integer[] fitness26 = new Integer[] { 0, 1, 2, 0, 0, 1 };
        fitness2.gene[0] = fitness21;
        fitness2.gene[1] = fitness22;
        fitness2.gene[2] = fitness23;
        fitness2.gene[3] = fitness24;
        fitness2.gene[4] = fitness25;
        fitness2.gene[5] = fitness26;
        System.out.println("Individual being evaluted:");
        printGene(fitness2);
        System.out.println("Expected fitness: 4");
        System.out.println("Actual Fitness: " + BabyLizardProblem.fitnessFunction(fitness2.gene));

    }

    // helper function to print the gene of an individual in matrix form
    public static void printGene(Individual individual) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(individual.gene[i][j] + "\t");
            }
            System.out.println();
        }
    }

}