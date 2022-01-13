package GA.Structure.Concrete;

import utils.GAUtils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class Scaling extends BaseContinousTransformation {

	@Override
	public String GetTitle() {
		return "s";
	}
	
	public Scaling()
	{
		super();
		
	
	}
	
	public Scaling(float val)
	{
		super(val);
		
	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new Scaling(_val);
	}
	
	@Override
	public float MinVal() {
		return GAUtils.MIN_SCALING;
	}

	@Override
	public float MaxVal() {
		return GAUtils.MAX_SCALING;
	}

	@Override
	public float ResetVal() {
		return GAUtils.RESET_SCALING;
	}

	@Override
	public double[] TransPoint(double[] point) {
		point[0] = _val*point[0];
		point[1] = _val*point[1];
		return point;
	}
	
	
}
