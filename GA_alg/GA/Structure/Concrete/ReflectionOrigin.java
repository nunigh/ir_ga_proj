package GA.Structure.Concrete;

import GA.Structure.Interface.BaseDiscreteTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ReflectionOrigin extends BaseDiscreteTransformation {
	
	
	@Override
	public String GetTitle() {
		return "reflection";
	}
	
	public ReflectionOrigin()
	{
		super();
	
	}
	
	public ReflectionOrigin(int index, float[] vals)
	{
		super(index, vals);
	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ReflectionOrigin(_index, _vals);
	}

	@Override
	public double[] TransPoint(double[] point) {
		double val = GetVal();
		point[0] *= val;
		point[1] *= val;
		return point;
	}

	@Override
	protected void initVals() {
		_vals = new float[] { -1, 1 };
		
	}


	
}
