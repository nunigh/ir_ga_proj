package GA.Structure.Concrete;

import utils.GAUtils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ShearingY extends BaseContinousTransformation {

	@Override
	public String GetTitle() {
		return "sheary";
	}
	
	public ShearingY()
	{
		super();
		
	
	}
	
	public ShearingY(float val)
	{
		super(val);
		
	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ShearingY(_val);
	}
	
	@Override
	public float MinVal() {
		return GAUtils.MIN_SHEAR;
	}

	@Override
	public float MaxVal() {
		return GAUtils.MAX_SHEAR;
	}

	@Override
	public float ResetVal() {
		return GAUtils.RESET_SHEAR;
	}
	@Override
	public double[] TransPoint(double[] point) {
		point[1] = _val*point[0] + point[1];
		return point;
	}
	
	
}
