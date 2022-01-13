package GA.Structure.Concrete;

import utils.GAUtils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ScalingY extends BaseContinousTransformation {

	@Override
	public String GetTitle() {
		return "sy";
	}
	
	public ScalingY()
	{
		super();
	}
	
	public ScalingY(float val)
	{
		super(val);	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ScalingY(_val);
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
		point[1] = _val*point[1];
		return point;
	}
	
	
}
