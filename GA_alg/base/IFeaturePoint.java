package base;

public interface IFeaturePoint {

	public IFeaturePoint clone();
	
	public double distance(IFeaturePoint other);
	
	public int size();
	
	public double get(int i);
	
//	public boolean zero();
	
}
