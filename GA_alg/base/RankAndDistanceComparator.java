package base;

import java.util.Comparator;

import Interfaces.Chromosome;

public class RankAndDistanceComparator implements Comparator<Chromosome>{
		 
	    @Override
	    public int compare(Chromosome i, Chromosome j) {
	    	
	    	   if (i.rank == null && j.rank == null) return 0;
			   if (i.rank != null && j.rank == null) return -1;
			   if (i.rank == null && j.rank != null) return 1;
			   
			   int val = 0;
			   val = Integer.compare(i.rank, j.rank);
			   if (val == 0)
			   {
				   val = Double.compare(-1*i.distance,-1*j.distance); // larger is better
			   }
			   return val;
	
	    }

}
