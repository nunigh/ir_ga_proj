package GA.Structure.Interface;

import java.awt.List;
import java.util.SortedSet;

import org.opencv.core.CvType;

import utils.Utils;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_core.*;

@SuppressWarnings("unused")
public abstract class BaseContinousTransformation extends BaseTransformation {

	protected float _val;
	
	public BaseContinousTransformation()
	{
		float val = GetRandVal();
		_val = val;
	}
	
	public BaseContinousTransformation(float val)
	{
		_val = val;
	}
	
	@Override
	public float GetVal()
	{
		return _val;
	}
	
	@Override
	public void SetVal(float value)//IBaseTransformation t)
	{
			//BaseContinousTransformation trans = (BaseContinousTransformation)t;
		//_val = trans.GetVal();
		_val = value;
	}
	
	public float GetRandVal()
	{
		float val = MinVal() + (float)(Utils.rand.nextDouble() * (MaxVal() - MinVal()));
		return val;
	}
	
	public float GetValWithNoise()
	{
		float noise = 0;
		
		double m = 0.01;
		
		double v = Utils.rand.nextDouble();
		if (v < 0.5)
		{
			m = 0.001;
		}
		
		noise = (float) ((float)Utils.rand.nextGaussian()*(MaxVal()-MinVal())*m);
		if (Utils.rand.nextDouble() > 0.5)
			noise *= -1;
		
		float val = _val + noise;
		
		// keep limits
		if (val < MinVal() || val > MaxVal())
			val = _val;
		
		/*if (val < MinVal())
			val = MinVal();
		else if (val > MaxVal())
			val = MaxVal();
		*/
		return val;
	}
	
	@Override
	public void Mutate()
	{
		float val;
		if (Utils.rand.nextDouble() < 0.1) // option #1: random value
		{
			val = GetRandVal();
		}
		else if (Utils.rand.nextDouble() < 0.1) // option #2: reset
		{
			val = ResetVal();
		}
		else //  option #3: noise
		{
			val = GetValWithNoise();
		}
		_val = val;
	}
	
	public abstract float ResetVal();
	public abstract float MinVal();
	public abstract float MaxVal();
	
}
