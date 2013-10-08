import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;

//This is the runner class for my genetic algorithm for image recognition
//The chromosome being evolved is the ability to recognize the "predator", represented by the bitmap below.
//The chromosome string of each organism will be compared to the predator bitmap, and fitness will depend on
//	the number of equal elements
public class ImageRecWilderness {
	
	// Fine tuning
	private static int populationSize = 1000; //Initial size of population
	private static int generationLimit = 100000; //Number of iterations to run
	
	//ArrayList of ImageRecOrganisms, representing the population of organisms we are evolving
	private static ArrayList<Organism> population;
	
	// "Predator" image for fitness function.
	private static int predator[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	                                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	                                  0, 0, 1, 1, 0, 0, 1, 1, 0, 0,
	                                  0, 0, 1, 1, 0, 0, 1, 1, 0, 0,
	                                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	                                  0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
	                                  0, 1, 0, 0, 1, 1, 0, 0, 1, 0,
	                                  0, 1, 1, 0, 0, 0, 0, 1, 1, 0,
	                                  0, 0, 1, 1, 1, 1, 1, 1, 0, 0,
	                                  0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }; // Truly fearsome, I know
	private static final int predatorWidth = 10;
	private static final int predatorHeight = 10;
	
	public static void main(String args[]){
		
		//Initialize the population
		System.out.println("Initializing Population...");
		population = new ArrayList<Organism>();
		generateInitialPopulation(populationSize);
		System.out.println("Population Initialized");
		
		//Start the circle of life
		System.out.println("Boom de yada boom de yada!");
		for (int generation = 0; generation < generationLimit; generation ++) {
			
			//For each member of the population, determine their fitness (ability to recognize the predator), 
			//		and set their fitness instance variable accordingly
			for (Organism org : population) {
				double fitness = determineFitness(org);
				org.setFitness(fitness);
			}
			
			//Sort the population based on fitness, highest fitness to lowest
			Collections.sort(population, Collections.reverseOrder());
			
			//Remove the bottom half of the population, select a number of random (but weighted by fitness) pairings from the remaining group 
			//	equal to half the number of organisms lost, and "breed" those organisms
			population.subList(population.size()/2, population.size()).clear();
			breedRandomPairs();
			
			System.out.println("Generation " + generation + " complete, " + 100*generation/generationLimit + "% complete.");
		}
		
		//Display the most fit organism's chromosome
		Organism mostFitOrganism = population.get(0);
		System.out.println(mostFitOrganism);
		System.out.println(mostFitOrganism.getFitness());
	}
	
	//Generates an initial population of ImageRecOrganisms with random chromosomes
	private static void generateInitialPopulation(int size) {
		for (int i = 0; i < populationSize; i++) {
			//First generate a new chromosome at random
			int algorithm[] = new int[predatorWidth*predatorHeight];
			for (int n = 0; n < predatorWidth*predatorHeight; n++) {
				int randBit = (int)(Math.random()*2);
				algorithm[n] = randBit;
			}
			Organism newOrganism = new Organism(algorithm);
			population.add(newOrganism);
		}
	}
	
	//Determine the fitness of an organism based on its ability to recognize a predator
	private static double determineFitness(Organism organism) {
		int chromosome[] = organism.getChromosome();
		double fitness = 0;
		
		for (int i = 0; i < predatorWidth*predatorHeight; i++) {
			if (chromosome[i] == predator[i]) {fitness++;}
		}
		
		return fitness;
	}
	
	//Generate new organisms based on the chromosomes of the most fit organisms
	private static void breedRandomPairs() {
		//Find the number of vacancies
		int numToBreed = populationSize - population.size();
		
		//For each vacancy, select two weighted random organisms to combine, and randomly combine their chomosomes, with a small chance for mutation
		for (int n = 0; n < numToBreed; n += 2) {
			Organism parent1 = getRandOrganism();
			Organism parent2 = getRandOrganism();
			
			Organism children[] = combine(parent1, parent2);
			children[0].mutate();
			children[1].mutate();
			
			population.add(children[0]);
			population.add(children[1]);
		}
	}
	
	//returns a randomly selected organism, with more fit organisms being more likely to be selected
	private static Organism getRandOrganism() {
		int randIndex = (int)(Math.random()*(populationSize/2));
		return population.get(randIndex);
	}
	
	//Randomly combines chromosomes from the two parent organisms to create a child
	private static Organism[] combine(Organism parent1, Organism parent2) {
		//First choose a random point along the length of the chromosome for genes to swap
		int crossOverPoint = (int)(Math.random()*predatorWidth*predatorHeight);
		
		//Now swap!
		Organism child1 = new Organism(ArrayUtils.addAll(Arrays.copyOfRange(parent1.getChromosome(), 0, crossOverPoint), Arrays.copyOfRange(parent2.getChromosome(), crossOverPoint, predatorWidth*predatorHeight)));
		Organism child2 = new Organism(ArrayUtils.addAll(Arrays.copyOfRange(parent2.getChromosome(), 0, crossOverPoint), Arrays.copyOfRange(parent1.getChromosome(), crossOverPoint, predatorWidth*predatorHeight)));
		
		//Return an array containing both children
		Organism children[] = {child1, child2};
		return children;
	}
	
}
