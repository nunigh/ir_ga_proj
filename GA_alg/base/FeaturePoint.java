package base;

public class FeaturePoint implements IFeaturePoint {
	
	double[] _features;
	
	public FeaturePoint(double[] features)
	{
		_features = new double[features.length];
		for (int i = 0; i < features.length; i++) {
			_features[i] = features[i]; 	
		}
	}
	
	@Override
	public IFeaturePoint clone()
	{
		return new FeaturePoint(_features);
	}
	
	@Override
	public int size()
	{
		return _features.length;
	}
	
	@Override
	public double get(int i)
	{
		return _features[i];
	}
	

	@Override
	public double distance(IFeaturePoint other)
	{
		double dist = 0;
		for (int i = 0; i < size(); i++) {
			dist += Math.pow(other.get(i) - this.get(i), 2);
		//	System.out.println(other.get(i) + " , " + this.get(i));
		}
		return Math.sqrt(dist);
	}
	
/*	@Override
	public boolean zero()
	{
		
		for (int i = 0; i < size(); i++) {
			if (this.get(i)!=0)
				return false;
		}
		return true;
	}
	*/
}
