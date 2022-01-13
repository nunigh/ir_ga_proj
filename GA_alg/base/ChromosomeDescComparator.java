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
public class ChromosomeDescComparator implements Comparator<Chromosome>{
	 
    @Override
    public int compare(Chromosome ch1, Chromosome ch2) {
        return  ch1.toString().compareTo(ch2.toString());
        
    }
}
