package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Interfaces.Chromosome;
import base.Population;

/**
 * 
 * RankBasedSelection class implements chromosome selection based on simple ranking of the entire population.
 * 
 * The population is numbered from 1... (size of population) 
 * and then ranked i^2 where i is the number of the chromosome in the population.
 *  
 * The probability for each chromosome is its relative rank.
 *
 */
public class RankBasedSelection extends Selection {
    
	// sum of rank of all chromosomes in the population
	private int _totalRank = 0;
	private List<Integer> _ranks = null;
	
    public RankBasedSelection(Population chromes) {
		super(chromes);
		
		// sort (by fitness)
		_chromes.Sort();
		_ranks = new ArrayList<Integer>();
		
		// sum fitness
		for (int i = 0; i < _chromes.Size(); i++) {
			_totalRank += Math.pow(i+1,2);
			for(int j=1;j<=_chromes.Size()-i; j++)
			{
				_ranks.add(i);
			}
		}
	}

    // returns a chromosome from the initial population based on chromosom's relative rank
  	public Chromosome Select2() {
  		
  		// select random number up to the total rank
          int rand = new Random().nextInt(_ranks.size());
          //System.out.println(_ranks.get(rand));
          return _chromes.GetChromosome(_ranks.get(rand));
      }


    // returns a chromosome from the initial population based on chromosom's relative rank
	public Chromosome Select() {
		
		// select random number up to the total rank
        int rand = new Random().nextInt(_totalRank);
        
        // calculate the index of the chromosome that matches the relative selected rank
        int val = 0;
        int index = 0;
        while (val <= rand && index < _chromes.Size()) {
            val += Math.pow(_chromes.Size() - index,2);
            index++;
        }
        
        // return the chromosome at the selected index
        if (index == 0) 
        	return _chromes.GetChromosome(0);
        
        //System.out.println(index);
        //System.out.println("selected: " + (index-1));
        return _chromes.GetChromosome(index-1);
    }
	
	
	
        
}
