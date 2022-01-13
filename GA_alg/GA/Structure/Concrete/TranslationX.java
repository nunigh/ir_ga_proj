package GA.Structure.Concrete;

import utils.GAUtils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class TranslationX extends BaseContinousTransformation {
	
	@Override
	public String GetTitle() {
		return "tx";
	}
	
	public TranslationX()
	{
		super();
	
		
	}
	
	public TranslationX(float val)
	{
		super(val);
		
		
	}

	@Override
	public IBaseTransformation clone() {
		return new TranslationX(_val);
	}

	@Override
	public float MinVal() {
		return GAUtils.MIN_TRANSLATION;
	}

	@Override
	public float MaxVal() {
		return GAUtils.MAX_TRANSLATION;
	}

	@Override
	public float ResetVal() {
		return GAUtils.RESET_TRANSLATION;
	}
	@Override
	public double[] TransPoint(double[] point) {
		point[0] += _val;
		return point;
	}
	
	
}
