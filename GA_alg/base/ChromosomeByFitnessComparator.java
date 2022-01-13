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
public class ChromosomeByFitnessComparator implements Comparator<Chromosome>{


    @Override
    public int compare(Chromosome ch1, Chromosome ch2) {
        int val = 0;
		try {
			if (ch1.get_fitness().GetValue(0) == ch2.get_fitness().GetValue(0))
			{
				val = Double.compare(-1*ch1.overlapPercent,-1*ch2.overlapPercent);
				return val;
			}
			
			val = Double.compare(ch1.get_fitness().GetValue(0),ch2.get_fitness().GetValue(0));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (val == 0)
        	val = Double.compare(-1*ch1.overlapPercent,-1*ch2.overlapPercent);
        if (val == 0)
        	val = ch1.compareTo(ch2);
        
        return val;
    }
}
