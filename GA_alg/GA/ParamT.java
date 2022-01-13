package GA;

import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class ParamT extends BaseContinousTransformation {

	private String _title = "";
	private int _i = -1;
	public ParamT(int i) {
		_i = i;
		_title = "param" + i;
	}
	
	public ParamT(int i, float val) {
		super(val);
		_i = i;
		_title = "param" + i;
	}
	
	
	@Override
	public float MinVal() {
		
			return (float) (-1*utils.Utils.size * 0.25);
		
	}

	@Override
	public float MaxVal() {
		
			return (float) (utils.Utils.size * 0.25);
		
	}
	
	
	@Override
	public float ResetVal() {
		
			return 0;
		
	}

	@Override
	public String GetTitle() {
		return _title;
	}

	@Override
	public double[] TransPoint(double[] point) {
		return null;
	}

	@Override
	public IBaseTransformation clone() {
		return new ParamT(_i, _val);
	}


	


	
	
	
}
