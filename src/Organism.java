import java.util.Arrays;


public class Organism implements Comparable<Organism>{

	private int chromosome[];
	private double fitness;
	
	public Organism(int newChromosome[]) {
		chromosome = newChromosome;
	}
	
	public int[] getChromosome() {
		return chromosome;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void setFitness(double newFitness) {
		fitness = newFitness;
	}

	public String toString() {
		return Arrays.toString(chromosome);
	}
	
	public int compareTo(Organism org) {
		if (fitness > org.getFitness()) { return 1; }
		else if (fitness < org.getFitness()) { return -1; }
		return 0;
	}
	
	//Mutates (inverts the bit) at a random number of locations (between 1 and 10, defaulting to 1 if the chromosome is less than 4 entries long
	public void mutate() {
		//Choose how many mutations to create
		int numMutations = (int)(Math.random()*10) + 1;
		if (chromosome.length < 4) { numMutations = 1; }
		
		//for each mutation, choose a random index, and invert the bit at that index
		for (int i = 0; i < numMutations; i++) {
			int randIndex = (int)Math.random()*chromosome.length;
			if(chromosome[randIndex] == 1) { chromosome[randIndex] = 0; }
			else { chromosome[randIndex] = 1; } //Yes, I could have used the ternary operator, but readability is more important than saving 1 line of code
		}
	}
}
