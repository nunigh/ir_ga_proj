package algorithms;

import java.util.ArrayList;
import java.util.Comparator;

import Interfaces.Chromosome;
import base.Fitness;
import base.Population;


public class PopulationManager {

	private Population _pop;
	
	private int fitnessUnchangedCount = 0;
	private boolean cont = true;
	private double mutationRate = CONST.MUTATE_NODE_PERCENT;
	private Fitness fitnessUnchanged = null;
	private String bestDescription = null;
	int bestIndex = 0;
	private ArrayList<Fitness> _fitnessHistory = new ArrayList<Fitness>(100);
	private int _historyCount = 0;
	private int _fitnessHistoryInitIndex = 0;
	
	public int getBestIndex() {
		return bestIndex;
	}

	public void setBestIndex(int bestIndex) {
		this.bestIndex = bestIndex;
	}

	public String getBestDescription() {
		return bestDescription;
	}

	public void setBestDescription(String bestDescription) {
		this.bestDescription = bestDescription;
	}

	public Fitness getFitnessUnchanged() {
		return fitnessUnchanged;
	}

	public void setFitnessUnchanged(Fitness fitnessUnchanged) {
		this.fitnessUnchanged = fitnessUnchanged;
	}

	
	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public int getFitnessUnchangedCount() {
		return fitnessUnchangedCount;
	}

	public void setFitnessUnchangedCount(int fitnessUnchangedCount) {
		this.fitnessUnchangedCount = fitnessUnchangedCount;
	}
	
	public void setNewFitness(Fitness val) throws Exception
	{
		_fitnessHistory.add(val);
		_historyCount++;		
	}
	
	public void setInitFitness()
	{
		_fitnessHistoryInitIndex = _fitnessHistory.size()-1;
	}
	
	public boolean isFitnessConverge() throws Exception
	{
		if (_historyCount == 0)
			return false;
		
		double last = _fitnessHistory.get(_historyCount-1).GetValue(0);
		if (last < 1.5)
			return true;
		
		double first = 0;
		if (_historyCount - _fitnessHistoryInitIndex < CONST.FITNESS_CONVERGENCE_NUM)
		{
			//first = _fitnessHistory.get(_fitnessHistoryInitIndex).GetValue(0);
			return false;
		}
		else
		{
			int index = (int) (_historyCount - CONST.FITNESS_CONVERGENCE_NUM);
			if (index > 0 && index <_fitnessHistory.size())
			{
				first =  _fitnessHistory.get(index).GetValue(0);
			}
			else
				{
				return false;
				}
		}
		
		return (first - last < CONST.FITNESS_CONVERGENCE_DELTA);
		
	}

	public boolean isCont() {
		return cont;
	}

	public void setCont(boolean cont) {
		//this.cont = cont;
	}
	
	public PopulationManager(int maxCapacity, Comparator<Chromosome> c)
	{
		_pop = new Population(maxCapacity, c);
	}
	
	public void setComparator(Comparator<Chromosome> c)
	{
		_pop.setComparator(c);
	}

	public Chromosome GetBestChromosome() {
		return _pop.GetBestChromosome();
	}

	public void AddChromosome(Chromosome ch) {
		_pop.AddChromosome(ch);
	}
	
	public void Sort() {
		_pop.Sort();
	}
	
	public void Sort(int index) {
		_pop.Sort(index);
	}
	
	public void RemoveAll() {
		_pop.RemoveAll(0);
	}

	public int Size() {
		return _pop.Size();
	}

	public Chromosome GetChromosome(int i) {
		return _pop.GetChromosome(i);
	}

	public Population get_pop() {
		return _pop;
	}

	public void SetChromosomesList(ArrayList<Chromosome> chroms) {
		_pop.SetChromosomesList(chroms);
		
	}

	public void Union(ArrayList<Chromosome> chroms) {
		_pop.Union(chroms);
		
	}

	public ArrayList<Chromosome> GetTopsChromosomes(int count) {
		return _pop.GetTopsChromosomes(count);
	}

	public Fitness GetAvg() throws Exception {
		return _pop.GetAvg();
	}
}
