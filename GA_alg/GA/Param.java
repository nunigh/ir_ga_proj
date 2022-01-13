package GA;

import GA.Structure.Interface.BaseContinousTransformation;
import GA.Structure.Interface.IBaseTransformation;

public class Param extends BaseContinousTransformation {

	private String _title = "";
	private int _i = -1;
	public Param(int i) {
		_i = i;
		_title = "param" + i;
	}
	
	public Param(int i, float val) {
		super(val);
		_i = i;
		_title = "param" + i;
	}
	
	
	@Override
	public float MinVal() {
	
			return -2;
	
	}

	@Override
	public float MaxVal() {
		
			return 2;
		
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
		return new Param(_i, _val);
	}

	


	
	
	
}
