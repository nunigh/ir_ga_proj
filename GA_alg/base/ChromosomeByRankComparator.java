package base;

import java.util.Comparator;

import Interfaces.Chromosome;

/**
 * 
 * The ChromosomeComparator is used to compare between chromosomes.
 * 
 * The comparison is done by comparing the chromosom's fitness value.
 *
 */
public class ChromosomeByRankComparator implements Comparator<Chromosome>{
	 
	RankAndDistanceComparator c = new RankAndDistanceComparator();
	
    @Override
    public int compare(Chromosome ch1, Chromosome ch2) {
    	
    	int val = c.compare(ch1, ch2);
        if (val == 0)
        	val = Double.compare(-1*ch1.overlapPercent,-1*ch2.overlapPercent);
        if (val == 0)
        	val = ch1.compareTo(ch2);
        
        return val;
    }
}
