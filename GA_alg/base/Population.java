package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Interfaces.Chromosome;

import utils.Utils;


/**
 * 
 * Population class represents a list of chromosomes.
 * 
 * The list of chromosomes are possible solutions/programs for the genetic programming algorithm.
 *
 */
public class Population {
	 
	// list of chromosomes
	private ArrayList<Chromosome> _pop;
	
	// comparator used for comparing chromosomes
	private Comparator<Chromosome> _comparator;
	private ChromosomeDescComparator _comparatorDesc;

	// get the population list
	public ArrayList<Chromosome> get_pop() {
		return _pop;
	}

	// initialize
	public Population(int maxCapacity, Comparator<Chromosome> c)
	{
		_pop = new ArrayList<Chromosome>(maxCapacity);
		_comparator = c;
		_comparatorDesc = new ChromosomeDescComparator();
	}
	
	public void setComparator(Comparator<Chromosome> c)
	{
		_comparator = c;
	}
	
	// add chromosome to the population
	public void AddChromosome(Chromosome ch)
	{
		_pop.add(ch);
	}
		
	// remove the chromosome at the given index from the population
	public Chromosome RemoveChromosome(int index)
	{
		return _pop.remove(index);
	}
	
	// add all chromosome starting at the given index from the population
	public boolean RemoveAll(int startIndex)
	{
		return _pop.removeAll(_pop.subList(startIndex, _pop.size()-1));
	}
	
	// get the chromosome at the specified index
	public Chromosome GetChromosome(int index)
	{
		return _pop.get(index);
	}
	
	// get the best chromosome in the population according to sort implementation (by fitness)
	public Chromosome GetBestChromosome()
	{
		Chromosome ch = null;
		if (_pop.size() > 0)
		{
			Sort();
			ch = _pop.get(0);
			
		/*	int index = 1;
			while (_pop.size() > index &&_pop.get(index).get_fitness() == ch.get_fitness())
			{
				if (_pop.get(index).GetDeep() > ch.GetDeep())
					ch = _pop.get(index);
				index++;
			}
			*/
		}
		return ch;
	}
	
	// set the population list with the given list
	public void SetChromosomesList(ArrayList<Chromosome> chroms)
	{
		_pop = chroms;
	}
	
	// calculate average fitness of the chromosomes in the population
	/*public double GetAvgFitness() {
		double sum = 0;
		for (Chromosome ch : _pop) 
			sum += ch.get_fitness();
		return sum/_pop.size();
	}
	*/
	
	// get the top chromosomes
	public ArrayList<Chromosome> GetTopsChromosomes(int count)
	{
		if (count >= _pop.size())
			return _pop;
					
		ArrayList<Chromosome> tops = new ArrayList<Chromosome>();
		Sort();
		for (int i = 0; i <count ; i++)
		{
			tops.add(_pop.get(i).clone());
		}
		return tops;
	}
	
	// sort the chromosomes list according to the comparator (by fitness)
	public void Sort()
	{
		Collections.sort(_pop, _comparator);
	}
	
	public void Sort(int index)
	{
		Collections.sort(_pop, new ChromosomePerValueComparator(index));
	}

		public void Sort(Comparator<Chromosome> comparator)
		{
			Collections.sort(_pop, comparator);
		}
	
	 
		public void SortByDesc()
		{
			Collections.sort(_pop, _comparatorDesc);
		}
	
	// add the given list of chromosomes to the populations
	public boolean Union(Population newPopulation)
	{
		return _pop.addAll(newPopulation.get_pop());
	}
	
	// add the given list of chromosomes to the populations
	public boolean Union(ArrayList<Chromosome> chroms)
	{
		return _pop.addAll(chroms);
	}
	
	// return the size of the population
	public int Size()
	{
		return _pop.size();
	}
	
	public Chromosome GetRandomChromosome() {
		return _pop.get(Utils.rand.nextInt(_pop.size()));
	}

	public Fitness GetAvg() throws Exception {
		
		return GetBestChromosome().get_fitness();
		
		/*double[] fitness = new double[_pop.get(0).get_fitness().Length()];
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = 0;
		}
		
		for (int i = 0; i < _pop.size(); i++) {
			Chromosome c = _pop.get(i);
			for (int j = 0; j < fitness.length; j++) {
				fitness[j] +=c.getValue(j);
			}
		}
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] /= _pop.size();
		}
		Fitness avg = new Fitness(fitness);
		return avg;*/
	}
}
