package GA.Structure.Interface;

import utils.Utils;


public abstract class BaseDiscreteTransformation extends BaseTransformation {

	protected float[] _vals = null;
	protected int _index = 0;
	
	public BaseDiscreteTransformation()
	{
		initVals();
		_index = GetRandIndex();
	}
	
	public BaseDiscreteTransformation(int index)
	{
		initVals();
		_index = index;
	}
	
	public BaseDiscreteTransformation(int index, float[] vals)
	{
		_vals = vals.clone();
		_index = index;
	}
	
	private int GetRandIndex()
	{
		return Utils.rand.nextInt(_vals.length);
	}
	
	@Override
	public float GetVal()
	{
		return GetVal(_index);
	}

	public int GetIndex()
	{
		return _index;
	}
	
	@Override
	public void SetVal(float value)//IBaseTransformation t)
	{
		_index = 0;
		//BaseDiscreteTransformation trans = (BaseDiscreteTransformation)t;
		for (int i = 0; i < _vals.length; i++) {
			if (_vals[i] == value)
			{
				_index = i;
				break;
			}
		}
//		_index = trans.GetIndex();
	}
	
	public float GetVal(int index)
	{
		return _vals[index];
	}
	
	public int GetIndexWithNoise()
	{
		int diff = 1;
		if (Utils.rand.nextDouble()<0.5)
			diff *= -1;
		
		int newIndex = _index + diff;
		if (newIndex < 0)
			newIndex = _vals.length - 1;
		
		newIndex = newIndex % _vals.length;
		return newIndex;
	}
	
	@Override
	public void Mutate()
	{
		int index;
		if (Utils.rand.nextDouble() < 0.1) // option #1: random value
		{
			index = GetRandIndex();
		}
		else //  option #2: noise
		{
			 index = GetIndexWithNoise();
		}
		_index = index;
	}

	
	protected abstract void initVals();
	
}
