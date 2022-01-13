package algorithms;


import Interfaces.Chromosome;
import base.ChromosomeByFitnessComparator;
import base.Population;

public class TournamentSelection extends Selection {

	public TournamentSelection(Population chromes) {
		super(chromes);
		
		/*_chromes.Sort();
		for (int i = 0; i < _chromes.Size(); i++) {
			_chromes.GetChromosome(i).rank = i;
		}*/
		
	}

	//select 7 random chromes and then select the best
	/*@Override
	public Chromosome Select() {
		Chromosome[] randList = new Chromosome[7];
		for (int i = 0; i < 7; i++) {
			randList[i] = _chromes.GetRandomChromosome();
		}
		Chromosome best = randList[0];
		for (int i = 1; i < 7; i++) {
			if (randList[i].get_rank() < best.get_rank())
				best = randList[i];
		}
		return best;
	}
*/
	
	@Override
	public Chromosome Select() {
		//Population randList = new Population(5, new ChromosomeByRankComparator());
		Population randList = new Population(3, new ChromosomeByFitnessComparator());
		for (int i = 0; i < 7; i++) {
			randList.AddChromosome(_chromes.GetRandomChromosome());
		}
		return randList.GetBestChromosome();
	}

}
