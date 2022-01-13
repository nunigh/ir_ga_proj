package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Interfaces.Chromosome;
import base.Population;
import base.RankAndDistanceComparator;

// http://nsga2-albp-sp2009.googlecode.com/svn/!svn/bc/49/trunk/src/nsga/Nsga2Algorithm.java

public class NSGARankBasedSelection extends Selection {
	
	// sum of rank of all chromosomes in the population
		//private int _totalRank = 0;
		
		private List<Integer> _ranks = null;
		
	 public NSGARankBasedSelection(Population chromes) throws Exception {
			super(chromes);
			
			List<List<Chromosome>> fronts = NSGAII.fastNondominatedSort(chromes.get_pop());
			
			ArrayList<Chromosome> population = new ArrayList<Chromosome>();
			int k = 0;
			while (population.size() < chromes.Size() && k < fronts.size()) {
				population.addAll(fronts.get(k));
				k += 1;		
			}
			//System.out.print("----");
			//print(population);
			NSGAII.crowdingDistanceAssignment(population);
			//System.out.print("----");
			//print(population);
			
			_chromes.SetChromosomesList(population);
			_chromes.Sort( new RankAndDistanceComparator());
			//System.out.print("----");
			//print(_chromes);
			
			_ranks = new ArrayList<Integer>();
			
			// sum fitness
			
			//int max = _chromes.GetChromosome(_chromes.Size()-1).rank;
			int size = _chromes.Size();
			for (int i = 0; i < size; i++) {
				
				//Chromosome ch = _chromes.GetChromosome(i);
				for(int j=0;j<= Math.pow(size - i,2); j++)
				{
					_ranks.add(i);
				}
			}
			
		
			
	 }
	 
	  // returns a chromosome from the initial population based on chromosom's relative rank
	  	public Chromosome Select() {
	  		
	  		
	  	// select random number up to the total rank
	  		int rand = new Random().nextInt(_ranks.size());
	  		int chosenIndex = _ranks.get(rand);
	  		//System.out.println("#### " + chosenIndex);
	  		return _chromes.GetChromosome(chosenIndex);
	  		
	        
	      }

  		private static void print(ArrayList<Chromosome> pop)
		{
  			StringBuilder str = new StringBuilder();
  			for (int i = 0; i < pop.size(); i++) {
  				Chromosome cc = pop.get(i);
				try {
					str.append(getPrintChrom(cc));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				str.append('\n');
  			}
  			System.out.print(str.toString());
		}
  		
  		private static void print(Population pop)
		{
  			StringBuilder str = new StringBuilder();
  			for (int i = 0; i < pop.Size(); i++) {
  				Chromosome cc = pop.GetChromosome(i);
				try {
					str.append(getPrintChrom(cc));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				str.append('\n');
  			}
  			System.out.print(str.toString());
		}
  		
  		private static String getPrintChrom(Chromosome cc) throws Exception {
  			StringBuilder str = new StringBuilder();
  			str.append("fitness: ");
  			str.append(cc.get_fitness());
  			
  			str.append(", overlap: ");
  			str.append(cc.overlapPercent);
  			str.append(", rank: ");
  			str.append(cc.rank);
  			str.append(", ");
  			str.append(cc.toString());
  			str.append(", ");
  			str.append(cc.getInfo());
  			
  			

  			return str.toString();
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
