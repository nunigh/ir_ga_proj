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
public class ChromosomePerValueComparator implements Comparator<Chromosome>{
	 
	int _i;
	public ChromosomePerValueComparator(int i)
	{
		_i = i;
	}
    @Override
   
        public int compare(Chromosome  o1, Chromosome  o2) {
    		
    		int val =0;
    		
            try {
				val = Double.compare(o1.getValue(_i),o2.getValue(_i));
			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return val;
        }
}
