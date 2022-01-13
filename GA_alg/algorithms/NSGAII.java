package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Interfaces.Chromosome;
import base.ChromosomePerValueComparator;

// http://nsga2-albp-sp2009.googlecode.com/svn/!svn/bc/49/trunk/src/nsga/Nsga2Algorithm.java

public abstract class NSGAII {

public static List<List<Chromosome>> fastNondominatedSort(ArrayList<Chromosome> P) throws Exception {  
        
		 for (Chromosome p: P) {
	            p.dominatedByNbr = 0;
	            p.dominateSet = new LinkedList<Chromosome>();
	            p.rank = null;
	        }
		
        List<Chromosome> front0 = new LinkedList<Chromosome>();
        for (Chromosome p: P) {
            for (Chromosome q: P) {
            	 int domination = 0;
            	try
            	{
                if (p == q)
                    continue;
                
                domination = p.checkDomination(q);
            	}
                catch (Exception e) {
            		System.out.println(e.getMessage());
				}
                if (domination == -1) {
                    p.dominateSet.add(q);
                }
                else if (domination == 1){
                    p.dominatedByNbr += 1;                  
                }
            }
            if (p.dominatedByNbr == 0) {
                p.rank = 0;
                front0.add(p);
            }
        }
        List<List<Chromosome>> fronts = new LinkedList<List<Chromosome>>();
        fronts.add(front0);
        
        int i = 0;
        while (fronts.get(i).size() > 0) {
            List<Chromosome> H = new LinkedList<Chromosome>();
            for (Chromosome p : fronts.get(i)) {
                for (Chromosome q: p.dominateSet) {
                    q.dominatedByNbr -= 1;
                    if (q.dominatedByNbr == 0) {
                        q.rank = i + 1;
                        H.add(q);
                    }
                }
            }
            i += 1;
            fronts.add(H);
        }
        return fronts;
    }

public static  void crowdingDistanceAssignment(ArrayList<Chromosome> P) { 

	
	if (P.size() == 0)
	{
		System.out.println("!!");
	}
	 for (Chromosome p: P) {
		 	p.distance = 0;
       }
	 
	
    int last = P.size()-1;
    int numOfValues = 2;
    for (int objNum = 0; objNum < numOfValues; objNum++) {
        // sorting using i'th objective values
    	
    	ChromosomePerValueComparator c = new ChromosomePerValueComparator(objNum);
        Collections.sort(P,c);
       
        P.get(0).distance = Double.MAX_VALUE;
        P.get(last).distance = Double.MAX_VALUE;
        for (int i = 1; i < last; i++) {
            try {
				P.get(i).distance += P.get(i+1).getValue(objNum) - P.get(i-1).getValue(objNum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
        }
    }
    
}

}
