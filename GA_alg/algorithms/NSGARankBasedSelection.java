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
	private int[] _ranks_hadar = null;
	int _sizeOfRankArray = 0;

	public NSGARankBasedSelection(Population chromes) throws Exception {
		super(chromes);
		if (CONST.PROFILING_MODE) {
			List<List<Chromosome>> fronts = NSGAII.fastNondominatedSort(chromes.get_pop());
			int chromesSize = chromes.Size();
			int frontsSize = fronts.size();
			ArrayList<Chromosome> population = new ArrayList<Chromosome>();
			int k = 0;
			int popsize=0;
			while (popsize< chromesSize && k < frontsSize) {
				List<Chromosome> kfronts = fronts.get(k);
				population.addAll(kfronts);
				++k;
				popsize+= kfronts.size();
			}
			NSGAII.crowdingDistanceAssignment(population);
			_chromes.SetChromosomesList(population);
			_chromes.Sort(new RankAndDistanceComparator());
			//_ranks = new ArrayList<Integer>();
			chromesSize = _chromes.Size();
			_sizeOfRankArray = 0;

			for (int i = 0; i < chromesSize; ++i) {
				int chromesSizeMinusI = chromesSize - i;
				double matpowlimit = chromesSizeMinusI*chromesSizeMinusI;
				_sizeOfRankArray+=matpowlimit + 1;
			} //maybe it also deterministic (if chorm size is deterministic)
			_ranks_hadar =new int[_sizeOfRankArray];
			int idx =-1;
			for (int i = 0; i < chromesSize; ++i) {
				int chromesSizeMinusI = chromesSize - i;
				double matpowlimit = chromesSizeMinusI*chromesSizeMinusI;
				for (int j = 0; j <= matpowlimit; ++j) {
					if (idx >= _sizeOfRankArray)
					{
						System.out.println("idx = "+idx + " _sizeOfRankArray = " + _sizeOfRankArray);
						int kl = 7;
					}
					try {
						_ranks_hadar[++idx] = i;
					}catch (Exception ex)
					{
						System.out.println("idx = "+idx + " _sizeOfRankArray = " + _sizeOfRankArray);
						int kl = 7;
					}
				}
			}
		}
		else {
			List<List<Chromosome>> fronts = NSGAII.fastNondominatedSort(chromes.get_pop());
			int chromesSize = chromes.Size();
			int frontsSize = fronts.size();
			ArrayList<Chromosome> population = new ArrayList<Chromosome>();
			int k = 0;
			while (population.size() < chromesSize && k < frontsSize) {
				population.addAll(fronts.get(k));
				++k;
			}
			//System.out.print("----");
			//print(population);
			NSGAII.crowdingDistanceAssignment(population);
			//System.out.print("----");
			//print(population);

			_chromes.SetChromosomesList(population);
			_chromes.Sort(new RankAndDistanceComparator());
			//System.out.print("----");
			//print(_chromes);

			_ranks = new ArrayList<Integer>();
			chromesSize = _chromes.Size();
			for (int i = 0; i < chromesSize; ++i) {

				double matpowlimit = Math.pow(chromesSize - i, 2);
				//Chromosome ch = _chromes.GetChromosome(i);
				for (int j = 0; j <= matpowlimit; ++j) {
					_ranks.add(i);
				}
			}
		}
	}

	final static Random Random = new Random();
	// returns a chromosome from the initial population based on chromosom's relative rank
	public Chromosome Select() {

		if (CONST.PROFILING_MODE)
		{
			int rand= Random.nextInt( _sizeOfRankArray);
			int chosenIndex = _ranks_hadar[rand];
			return _chromes.GetChromosome(chosenIndex);
		} else {
			// select random number up to the total rank
			int rand = new Random().nextInt(_ranks.size());
			int chosenIndex = _ranks.get(rand);
			//System.out.println("#### " + chosenIndex);
			return _chromes.GetChromosome(chosenIndex);
		}
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
