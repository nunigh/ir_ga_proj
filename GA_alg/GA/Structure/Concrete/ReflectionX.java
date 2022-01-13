package GA.Structure.Concrete;

import GA.Structure.Interface.BaseDiscreteTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ReflectionX extends BaseDiscreteTransformation {
	
	
	@Override
	public String GetTitle() {
		return "reflectionx";
	}
	
	public ReflectionX()
	{
		super();
	
	}
	
	public ReflectionX(int index, float[] vals)
	{
		super(index, vals);
	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ReflectionX(_index, _vals);
	}

	@Override
	public double[] TransPoint(double[] point) {
		point[0] *= GetVal();
		return point;
	}

	@Override
	protected void initVals() {
		_vals = new float[] { -1, 1 };
		
	}


	
}
