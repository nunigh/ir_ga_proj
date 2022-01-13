package base;

public class Fitness implements Comparable<Fitness>{



	double[] _fitness;
	
	public Fitness(double[] fitness)
	{
		_fitness = fitness.clone();
	}
	
	public Fitness clone() {
		return new Fitness(_fitness);
	}
	
	public int Length()
	{
		return _fitness.length;
	}
	
	public double GetValue(int i) throws Exception
	{
		if (i > _fitness.length - 1)
			throw new Exception("Invalid fitness index");
		
		return _fitness[i];
	}	
	
	public boolean dominate(Fitness f) throws Exception {
	    
		boolean result = true;
		boolean one_better = false;
		
		for (int i=0; i<f.Length(); i++)
		{
			if (GetValue(i) > f.GetValue(i)) // if at least one is worse
			{
					//result = false;
				return false;
			}
			
			if (GetValue(i) < f.GetValue(i)) // if at least one is better - we want to minimize
					one_better = true;
		}
		
		if (!one_better)
			result = false;

		return result;
	}

	@Override
	public String toString() {
		
		StringBuilder s = new StringBuilder();
		int l = Length();
		for (int i=0; i<l; i++)
		{
			try {
				s.append(GetValue(i));
				if (i < l-1)
					s.append(",");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return s.toString();
	}
	
	@Override
	public int compareTo(Fitness o) {
		int l = Length();
		for (int i=0; i<l; i++)
		{
			try {
				int val = Double.compare(GetValue(i), o.GetValue(i)) ;
				if (val != 0)
					return val;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return 0;
		
	}

}
