package GA.Structure.Concrete;

import utils.GAUtils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ShearingX extends BaseContinousTransformation {

	@Override
	public String GetTitle() {
		return "shearx";
	}
	
	public ShearingX()
	{
		super();
		
	
	}
	
	public ShearingX(float val)
	{
		super(val);
		
	
	}
	
	@Override
	public IBaseTransformation clone() {
		return new ShearingX(_val);
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
		point[0] = _val*point[1] + point[0];
		return point;
	}
	
}
