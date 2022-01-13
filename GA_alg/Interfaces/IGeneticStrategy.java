package Interfaces;

import java.util.ArrayList;

import org.opencv.core.Point;


public interface IGeneticStrategy {
	
	Chromosome CreateChromosome();
	
	Chromosome CreateChromosome(Chromosome ch1, Chromosome ch2);
	
	Chromosome[] CrossOver(Chromosome c1, Chromosome c2);
	
	void Mutation(Chromosome ch, double mutationRate);
	
	public double CalculateRMSE(
			
			ArrayList<Point> A, ArrayList<Point> B, 
			Chromosome ch,
			StringBuilder output, Boolean print);	
}
