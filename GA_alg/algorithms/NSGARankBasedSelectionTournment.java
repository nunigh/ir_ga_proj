package algorithms;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

import Interfaces.Chromosome;
import base.ChromosomeByFitnessComparator;
import base.Population;
import base.RankAndDistanceComparator;

// http://nsga2-albp-sp2009.googlecode.com/svn/!svn/bc/49/trunk/src/nsga/Nsga2Algorithm.java

public class NSGARankBasedSelectionTournment extends Selection {
	
	// sum of rank of all chromosomes in the population
		//private int _totalRank = 0;
		
		//private List<Integer> _ranks = null;
		
	 public NSGARankBasedSelectionTournment(Population chromes) throws Exception {
			super(chromes);
			
			List<List<Chromosome>> fronts = NSGAII.fastNondominatedSort(chromes.get_pop());
			ArrayList<Chromosome> population = new ArrayList<Chromosome>();
			int k = 0;
			while (population.size() < chromes.Size()) {
				population.addAll(fronts.get(k));
				k += 1;		
			}
			
			NSGAII.crowdingDistanceAssignment(population);
			
			_chromes.SetChromosomesList(population);
			_chromes.Sort( new RankAndDistanceComparator());
			
		
			
	 }
	 
	  // returns a chromosome from the initial population based on chromosom's relative rank
	  	public Chromosome Select() {
	  		
	  	
	        Population randList = new Population(3, new ChromosomeByFitnessComparator());
	  		for (int i = 0; i < 3; i++) {
	  			
	  			randList.AddChromosome(_chromes.GetRandomChromosome());
	  		}
	  		
	  		int bestRank = randList.GetBestChromosome().get_rank();
	  		ArrayList<Integer> a = new ArrayList<Integer>(3);
	  		for (int i = 0; i < randList.Size(); i++) {
				if (randList.GetChromosome(i).rank <= bestRank)
					a.add(i);
			}
	  	   int rand = Utils.rand.nextInt(a.size());
	  	 System.out.println("#################### " + a.get(rand));
	  	   return randList.GetChromosome(a.get(rand));
	        
	      }



	    // returns a chromosome from the initial population based on chromosom's relative rank
	/*	public Chromosome Select3() {
			
			// select random number up to the total rank
	        int rand = new Random().nextInt(_totalRank);
	        
	        // calculate the index of the chromosome that matches the relative selected rank
	        int val = 0;
	        int index = 0;
	        while (val <= rand && index < _chromes.Size()) {
	            val += Math.pow(_chromes.Size() - index,1);
	            index++;
	        }
	        
	        return _chromes.GetChromosome(index-1);
	    }
	    */
}
