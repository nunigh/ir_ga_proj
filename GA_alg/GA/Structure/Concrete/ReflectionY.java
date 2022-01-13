package GA.Structure.Concrete;

import GA.Structure.Interface.BaseDiscreteTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ReflectionY extends BaseDiscreteTransformation {
	
	
	@Override
	public String GetTitle() {
		return "reflectiony";
	}
	
	public ReflectionY()
	{
		super();
	
	}
	
	public ReflectionY(int index, float[] vals)
	{
		super(index, vals);
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ReflectionY(_index, _vals);
	}

	@Override
	public double[] TransPoint(double[] point) {
		point[1] *= GetVal();
		return point;
	}

	@Override
	protected void initVals() {
		_vals = new float[] { -1, 1 };
		
	}


	
}
