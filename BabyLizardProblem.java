// Baby Lizard Problem
// Brianna McDonald 2020

import java.util.*;

public class BabyLizardProblem {
    public static int boardSize;
    public static int popSize;
    public static int maxNumOfGens;
    // indices where there is a tree when mapping from 2d array to 1d array
    public static int[] trees;
    public static Individual[] population;
    public static Individual bestSolution;

    public static final int LIZARD = 1;
    public static final int TREE = 2;
    public static int numOfGens = 0;
    public static int minFitness = 0;
    public static int bestFitness = Integer.MAX_VALUE;

    public static void main(String[] args) {
        // use default values
        boardSize = 8;
        popSize = 200;
        maxNumOfGens = 10000;
        trees = new int[] { 7, 21, 32, 35, 52 };
        population = new Individual[popSize];
        bestSolution = new Individual(boardSize);

        initialize();
        // while termination condition is not satisfied
        while (bestFitness != minFitness && numOfGens <= maxNumOfGens) {
            // select parents
            Individual[] parentPop = parentSelection(popSize);
            // recombine parents
            Individual[] childrenPop = createChildren(parentPop, popSize * 2);
            // mutate offspring
            mutateChildren(childrenPop, 0.6);
            updateFitnesses(childrenPop);
            // evaluate offspring and see if any of them are perfect solutions
            updateBestFitness(childrenPop);
            if (bestFitness == minFitness) {
                break;
            }
            // select survivors, a mix of the best children and random individuals
            Individual[] bestChildrenPop = findBestChildren(childrenPop, 0.6);
            Individual[] randomPop = createRandomPop(0.4);
            // update the population
            population = Arrays.copyOf(bestChildrenPop, bestChildrenPop.length + randomPop.length);
            System.arraycopy(randomPop, 0, population, bestChildrenPop.length, randomPop.length);
            // increment number of generations completed
            numOfGens++;
        }
        if (bestFitness == minFitness) {
            // display the best solution found
            Window window = new Window(boardSize, bestFitness, numOfGens, bestSolution);
        } else {
            System.out.println("Solution could not be found");
        }
    }

    // finds if the given index is a tree or not
    public static Boolean isInTrees(int index) {
        for (int i = 0; i < trees.length; i++) {
            if (trees[i] == index) {
                return true;
            }
        }
        return false;
    }

    // finds and returns the indices in a given row that contain a lizard
    public static ArrayList<Integer> findLizards(Integer[] row) {
        ArrayList<Integer> lizards = new ArrayList<Integer>();
        for (int i = 0; i < row.length; i++) {
            if (row[i] == LIZARD) {
                lizards.add(i);
            }
        }
        return lizards;
    }

    // given a population of individuals, updates the fitness of any individual
    // whose fitness value does not match the actual fitness of its gene
    public static void updateFitnesses(Individual[] pop) {
        for (int i = 0; i < pop.length; i++) {
            int actualFitness = fitnessFunction(pop[i].gene);
            if (actualFitness != pop[i].fitness) {
                pop[i].fitness = actualFitness;
            }
        }
    }

    public static Individual createRandomIndividual() {
        Random rand = new Random();
        int numOfLizards = 0;
        Individual individual = new Individual(boardSize);
        // randomly place the correct number of lizards, making sure not to
        // replace any trees
        while (numOfLizards < boardSize) {
            Boolean isTree = true;
            int value1 = 0;
            int value2 = 0;
            // make sure we don't replace a tree
            while (isTree) {
                value1 = rand.nextInt(boardSize);
                value2 = rand.nextInt(boardSize);
                if (!isInTrees(value1 * boardSize + value2)) {
                    isTree = false;
                }
            }
            // check if this position already holds a lizard
            if (individual.gene[value1][value2] != null && individual.gene[value1][value2] == LIZARD) {
                continue;
            } else {
                // add lizard in the randomly selected place
                individual.gene[value1][value2] = LIZARD;
                numOfLizards++;
            }
        }
        // go through the gene and fill in the rest with trees and empty spaces in
        // the appropriate places
        for (int j = 0; j < boardSize; j++) {
            for (int k = 0; k < boardSize; k++) {
                if (individual.gene[j][k] == null || individual.gene[j][k] != 1) {
                    if (isInTrees(j * boardSize + k)) {
                        individual.gene[j][k] = TREE;
                    } else {
                        individual.gene[j][k] = 0;
                    }
                }
            }
        }
        individual.fitness = fitnessFunction(individual.gene);
        return individual;
    }

    // initialize the population with random individuals
    public static void initialize() {
        for (int i = 0; i < popSize; i++) {
            population[i] = createRandomIndividual();
        }
    }

    // iterate through the given population to check if it contains an individual
    // with a higher fitness than the current best fitness found
    public static void updateBestFitness(Individual[] pop) {
        for (int i = 0; i < pop.length; i++) {
            int fitness = pop[i].fitness;
            if (fitness < bestFitness) {
                bestFitness = fitness;
                bestSolution = pop[i];
            }
            if (fitness == minFitness) {
                break;
            }
        }
    }

    // given two points on the grid and whether they are in the same row, column, or
    // diagonal, check if there is a tree between them
    public static Boolean checkForTrees(int index1, int index2, String type) {
        if (type == "row") {
            for (int treeIndex : trees) {
                if (((index1 < treeIndex) && (treeIndex < index2)) || ((index2 < treeIndex) && (treeIndex < index1))) {
                    return true;
                }
            }
        } else if (type == "column") {
            for (int treeIndex : trees) {
                if ((((index1 < treeIndex) && (treeIndex < index2)) || ((index2 < treeIndex) && (treeIndex < index1)))
                        && ((index1 % boardSize) == (treeIndex % boardSize))) {
                    return true;
                }
            }
        } else if (type == "diagonal") {
            for (int treeIndex : trees) {
                if ((((index1 < treeIndex) && (treeIndex < index2)) || ((index2 < treeIndex) && (treeIndex < index1)))
                        && ((index1 % (boardSize + 1)) == (treeIndex % (boardSize + 1)))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int fitnessFunction(Integer[][] genotype) {
        // iterate through the genotype until you find a lizard
        // for every lizard look u, d, l, r, ul, ur, dl, dr
        // if there is another lizard in its path, add 0.5 to fitness
        // unless there is a tree between them

        double fitness = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (genotype[i][j] == LIZARD) {
                    int rowIndex = i;
                    int colIndex = j;
                    // look down
                    while (rowIndex < boardSize) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            // if there are no trees between them, increase fitness
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "column")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        rowIndex++;
                    }
                    rowIndex = i;
                    colIndex = j;
                    // look up
                    while (rowIndex >= 0) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "column")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        rowIndex--;
                    }
                    rowIndex = i;
                    colIndex = j;
                    // look left
                    while (colIndex >= 0) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "row")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        colIndex--;
                    }
                    rowIndex = i;
                    colIndex = j;
                    // look right
                    while (colIndex < boardSize) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "row")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        colIndex++;
                    }
                    rowIndex = i;
                    colIndex = j;
                    // look up-left
                    while (colIndex >= 0 && rowIndex >= 0) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "diagonal")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        colIndex--;
                        rowIndex--;
                    }
                    colIndex = j;
                    rowIndex = i;
                    // look up-right
                    while (colIndex < boardSize && rowIndex >= 0) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "diagonal")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        colIndex++;
                        rowIndex--;
                    }
                    colIndex = j;
                    rowIndex = i;
                    // look down-left
                    while (colIndex >= 0 && rowIndex < boardSize) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "diagonal")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        colIndex--;
                        rowIndex++;
                    }
                    colIndex = j;
                    rowIndex = i;
                    // look down-right
                    while (colIndex < boardSize && rowIndex < boardSize) {
                        if (genotype[rowIndex][colIndex] == LIZARD && !(rowIndex == i && colIndex == j)) {
                            if (!checkForTrees((i * boardSize + j), (rowIndex * boardSize + colIndex), "diagonal")) {
                                fitness = fitness + 0.5;
                            }
                        }
                        colIndex++;
                        rowIndex++;
                    }
                }
            }
        }
        return (int) Math.ceil(fitness);
    }

    // choose n random members of the population and return the one with the highest
    // fitness
    public static Individual tournamentSelection(int n) {
        int bestFitness = Integer.MAX_VALUE;
        Individual bestSol = population[1];
        for (int i = 0; i < n; i++) {
            // pick a random sol
            Random rand = new Random();
            int index = rand.nextInt(popSize);
            Individual candidate = population[index];
            // evaluate its fitness
            if (candidate.fitness < bestFitness) {
                bestFitness = candidate.fitness;
                bestSol = candidate;
            }
        }
        return bestSol;
    }

    // choose n parents using tournament selection
    public static Individual[] parentSelection(int n) {
        Individual[] parentPop = new Individual[n];
        for (int i = 0; i < n; i++) {
            parentPop[i] = tournamentSelection(10);
        }
        return parentPop;
    }

    // make children start as a copy of one of the parents, chosen randomly.
    // for each row in child, check if the row in the other parent has the
    // number of lizards.
    // if these rows have the same number of lizards, swap the position(s)
    // of the lizard(s) in that row of the child.
    // also make sure not to replace a tree.
    public static Individual recombine(Individual parent0, Individual parent1) {
        Random rand = new Random();
        Individual child = new Individual(boardSize);
        int parentNum = rand.nextInt(2);
        // set child to one of the parents, randomly chosen
        if (parentNum == 0) {
            System.arraycopy(parent0.gene, 0, child.gene, 0, parent0.gene.length);
        } else {
            System.arraycopy(parent1.gene, 0, child.gene, 0, parent1.gene.length);
        }
        // for each row in the child
        for (int i = 0; i < boardSize; i++) {
            // check if the two rows have the same number of lizards
            ArrayList<Integer> lizardArray0 = findLizards(parent0.gene[i]);
            ArrayList<Integer> lizardArray1 = findLizards(parent1.gene[i]);
            if (lizardArray0.size() == lizardArray1.size()) {
                // swap position(s) of the lizard(s)
                for (int k = 0; k < lizardArray1.size(); k++) {
                    // check for trees
                    int index0 = lizardArray0.get(k);
                    int index1 = lizardArray1.get(k);
                    if (index0 == index1) {
                        continue;
                    }
                    if (isInTrees(i * boardSize + index0) || isInTrees(i * boardSize + index1)) {
                        continue;
                    }
                    // add in new lizard and replace the old one with a 0
                    if (parentNum == 0) {
                        if (child.gene[i][index1] == LIZARD || child.gene[i][index0] == LIZARD) {
                            continue;
                        }
                        child.gene[i][index1] = LIZARD;
                        child.gene[i][index0] = 0;
                    } else {
                        if (child.gene[i][index1] == LIZARD || child.gene[i][index0] == LIZARD) {
                            continue;
                        }
                        child.gene[i][index0] = LIZARD;
                        child.gene[i][index1] = 0;
                    }
                }
            }
        }
        child.fitness = fitnessFunction(child.gene);
        return child;
    }

    // create n/2 children from the parents in the given parent population thorugh
    // recombination
    public static Individual[] createChildren(Individual[] parentPop, int n) {
        Individual[] childrenPop = new Individual[(int) Math.floor(n / 2)];
        int cIndex = 0;
        for (int i = 0; i < Math.floor(n / 2); i = i + 2) {
            Individual parent0 = parentPop[i];
            Individual parent1 = parentPop[i + 1];
            childrenPop[cIndex] = recombine(parent0, parent1);
            childrenPop[cIndex + 1] = recombine(parent1, parent0);
            cIndex = cIndex + 2;
        }
        return childrenPop;
    }

    // mutate each of the children in the given chhild population with probability p
    public static Individual[] mutateChildren(Individual[] childPop, double p) {
        for (int i = 0; i < childPop.length; i++) {
            mutation(childPop[i], p);
        }
        return childPop;
    }

    // find the top given percent of the given child population
    public static Individual[] findBestChildren(Individual[] childPop, double percent) {
        int numOfChildren = (int) Math.floor(percent * popSize);
        Individual[] newChildPop = new Individual[numOfChildren];

        List<Individual> childPopList = Arrays.asList(childPop);
        childPopList.sort((Individual c1, Individual c2) -> c1.fitness - c2.fitness);
        for (int i = 0; i < numOfChildren; i++) {
            newChildPop[i] = childPopList.get(i);
        }

        return newChildPop;
    }

    // creates a population of random individuals that has a size of the given
    // percentage of the population size
    public static Individual[] createRandomPop(double percent) {
        int numOfRandom = (int) Math.round(percent * popSize);
        Individual[] randomPop = new Individual[numOfRandom];

        for (int i = 0; i < numOfRandom; i++) {
            Individual newIndividual = createRandomIndividual();
            randomPop[i] = newIndividual;
        }
        return randomPop;
    }

    // with probability p, randomly select two values in the given individual's gene
    // to swap, making sure not to move or replace a tree
    public static void mutation(Individual individual, double p) {
        Random rand = new Random();
        double newDouble = rand.nextDouble();

        if (newDouble <= p) {
            Boolean isTree1 = true;
            Boolean isTree2 = true;
            int i = 0, j = 0, k = 0, l = 0;
            // making sure we don't move a tree
            while (isTree1) {
                i = rand.nextInt(boardSize);
                j = rand.nextInt(boardSize);
                if (!isInTrees(i * boardSize + j)) {
                    isTree1 = false;
                }
            }
            while (isTree2) {
                k = rand.nextInt(boardSize);
                l = rand.nextInt(boardSize);
                if (!isInTrees(k * boardSize + l)) {
                    isTree2 = false;
                }
            }
            // swap values
            int temp = individual.gene[i][j];
            individual.gene[i][j] = individual.gene[k][l];
            individual.gene[k][l] = temp;
            individual.fitness = fitnessFunction(individual.gene);
        }
    }
}