package algorithms;

import java.util.Random;

import Interfaces.Chromosome;
import base.Population;

/**
 * 
 * RolleteWheelSelection class implements chromosome selection based on fitness of each chromosome in the population.
 * 
 * Each chromosome gets its probability from its relative fitness.
 *
 */
public class RolleteWheelSelection extends Selection {

	// sum of fitness of all chromosomes in the population
	private double _totalFitness = 0;
	
	public RolleteWheelSelection(Population chromes) throws Exception {
		super(chromes);
		
		// sort (by fitness)
		_chromes.Sort();
		
		// sum fitness
		for (int i = 0; i < _chromes.Size(); i++) {
			_totalFitness += _chromes.GetChromosome(i).get_fitness().GetValue(0);//get_rank();
		}
	}

	// returns a chromosome from the initial population based on chromosom's relative fitness
	public Chromosome Select() throws Exception {
		
		// select random number up to the total fitness
	    double rand = new Random().nextInt((int) _totalFitness);
        
	    // calculate the index of the chromosome that matches the relative selected fitness 
        int val = 0;
        int index = _chromes.Size();
        while (val <= rand && index > 0) {
        	index--;
        	val += _chromes.GetChromosome(index).get_fitness().GetValue(0);//.get_rank();
        }
        // since the chromes are sorted from the best to the worst - reverse index
        index = _chromes.Size() - index;
        
        // return the chromosome at the selected index
        if (index == _chromes.Size()) return _chromes.GetChromosome(0);
       
        return _chromes.GetChromosome(index);
    }

}
