package GA.Structure.Concrete;

import utils.GAUtils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ScalingX extends BaseContinousTransformation {

	@Override
	public String GetTitle() {
		return "sx";
	}
	
	public ScalingX()
	{
		super();
		
	
	}
	
	public ScalingX(float val)
	{
		super(val);
		
	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ScalingX(_val);
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
		return point;
	}

}
