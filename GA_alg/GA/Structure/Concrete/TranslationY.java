package GA.Structure.Concrete;

import utils.GAUtils;
import utils.Utils;
import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class TranslationY extends BaseContinousTransformation {	
	
	@Override
	public String GetTitle() {
		return "ty";
	}
	
	public TranslationY()
	{
		super();
	}
	
	public TranslationY(float val)
	{
		super(val);
		
		
	}
	
	@Override
	public IBaseTransformation clone() {
		return new TranslationY(_val);
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
		point[1] += _val;
		return point;
	}
	
}
